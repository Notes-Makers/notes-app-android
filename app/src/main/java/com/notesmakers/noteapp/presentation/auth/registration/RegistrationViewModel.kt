package com.notesmakers.noteapp.presentation.auth.registration

import androidx.lifecycle.viewModelScope
import com.notesmakers.auth.domain.AuthErrorMapper
import com.notesmakers.noteapp.data.auth.EmailInputError
import com.notesmakers.noteapp.data.auth.InputError
import com.notesmakers.noteapp.data.auth.NameInputError
import com.notesmakers.noteapp.data.auth.NicknameInputError
import com.notesmakers.noteapp.data.auth.PasswordInputError
import com.notesmakers.noteapp.data.auth.SurnameInputError
import com.notesmakers.noteapp.domain.auth.GetAuthErrorMapperUseCase
import com.notesmakers.noteapp.domain.auth.RegisterUserUseCase
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class RegistrationViewModel(
    private val registerUseCase: RegisterUserUseCase,
    private val authErrorMapper: GetAuthErrorMapperUseCase,
) : BaseViewModel() {

    private val _registerState = MutableStateFlow<RegisterState>(RegisterState.None)
    val registerState = _registerState.asStateFlow()

    fun register(
        name: String,
        surname: String,
        email: String,
        nickname: String,
        password: String
    ) {
        validate(
            listOf(
                EmailInputError(email),
                PasswordInputError(password),
                NameInputError(name),
                NicknameInputError(nickname),
                SurnameInputError(surname),
            )
        ) {
            viewModelScope.launch {
                runCatching {
                    registerUseCase(
                        email = email,
                        password = password,
                        name = name,
                        nickname = nickname,
                        surname = surname
                    )
                }.onFailure {
                    sendMessageEvent(MessageEvent.Error(authErrorMapper(it)))
                }.onSuccess {
                    sendMessageEvent(MessageEvent.Success)
                }
            }
        }
    }

    private fun validate(inputsErrors: List<InputError>, onSuccess: () -> Unit) {
        val errors: MutableList<InputError> = mutableListOf()
        inputsErrors.forEach {
            it.validate()
            if (it.hasErrors()) errors.add(it)
        }
        if (errors.isEmpty()) {
            _registerState.value = RegisterState.None
            onSuccess()
        } else {
            _registerState.value = RegisterState.Error(errors)
        }
    }

    sealed interface RegisterState {
        data object None : RegisterState
        data class Error(val errors: List<InputError>) : RegisterState
    }
}

