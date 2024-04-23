package com.notesmakers.noteapp.presentation.home

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.domain.auth.CheckUserSignInStatusUseCase
import com.notesmakers.noteapp.domain.auth.LogoutUseCase
import com.notesmakers.noteapp.domain.notes.GetNotesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    val checkUserSignInStatusUseCase: CheckUserSignInStatusUseCase,
    val logoutUseCase: LogoutUseCase,
    getNotesUseCase: GetNotesUseCase,
) : BaseViewModel() {
    var notesEventFlow = getNotesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _userIsLoggedIn = MutableStateFlow(checkUserSignInStatusUseCase())
    val userIsLoggedIn = _userIsLoggedIn.asStateFlow()

    fun logout() {
        viewModelScope.launch {
            runCatching {
                logoutUseCase()
            }.onSuccess {
                _userIsLoggedIn.value = checkUserSignInStatusUseCase()
            }
                .onFailure {
                }
        }
    }
}
