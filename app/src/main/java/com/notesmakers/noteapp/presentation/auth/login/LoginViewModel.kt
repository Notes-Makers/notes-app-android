package com.notesmakers.noteapp.presentation.auth.login

import androidx.lifecycle.viewModelScope
import com.notesmakers.auth.domain.AuthErrorMapper
import com.notesmakers.noteapp.data.auth.EmailInputError
import com.notesmakers.noteapp.data.auth.InputError
import com.notesmakers.noteapp.data.auth.PasswordInputError
import com.notesmakers.noteapp.domain.auth.GetAuthErrorMapperUseCase
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.domain.auth.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val authErrorMapper: GetAuthErrorMapperUseCase,
) : BaseViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.None)
    val loginState = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        validate(
            listOf(
                EmailInputError(email),
                PasswordInputError(password)
            )
        ) {
            viewModelScope.launch {
                runCatching {
                    loginUseCase(email = email, password = password)
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
            _loginState.value = LoginState.None
            onSuccess()
        } else {
            _loginState.value = LoginState.Error(errors)
        }
    }

    sealed interface LoginState {
        data object None : LoginState
        data class Error(val errors: List<InputError>) : LoginState
    }
}

