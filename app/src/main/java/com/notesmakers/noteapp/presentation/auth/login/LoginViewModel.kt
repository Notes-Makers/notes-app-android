package com.notesmakers.noteapp.presentation.auth.login

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.domain.auth.LoginUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class LoginViewModel(
    private val loginUseCase: LoginUseCase
) : BaseViewModel() {
    fun login(email: String, password: String) {
        viewModelScope.launch {
            runCatching {
                loginUseCase(email = email, password = password)
            }.onFailure {
                sendMessageEvent(MessageEvent.Error("Something gone wrong"))
            }.onSuccess {
                sendMessageEvent(MessageEvent.Success)
            }
        }
    }
}