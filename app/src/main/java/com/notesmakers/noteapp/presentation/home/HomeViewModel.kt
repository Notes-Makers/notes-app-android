package com.notesmakers.noteapp.presentation.home

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.data.notes.Note
import com.notesmakers.noteapp.domain.auth.CheckUserSignInStatusUseCase
import com.notesmakers.noteapp.domain.auth.LogoutUseCase
import com.notesmakers.noteapp.domain.notes.DeleteNoteByIdUseCase
import com.notesmakers.noteapp.domain.notes.GetNotesUseCase
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    val checkUserSignInStatusUseCase: CheckUserSignInStatusUseCase,
    val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
    val logoutUseCase: LogoutUseCase,
    getNotesUseCase: GetNotesUseCase,
) : BaseViewModel() {
    var notesEventFlow = getNotesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        emptyList()
    )

    private val _userIsLoggedIn = MutableStateFlow(checkUserSignInStatusUseCase())
    val userIsLoggedIn = _userIsLoggedIn.asStateFlow()

    private val _selectedNote = MutableStateFlow<NoteSelectedStatus>(NoteSelectedStatus.None)
    val selectedNote = _selectedNote.asStateFlow()
    fun checkUserSignIn() {
        _userIsLoggedIn.value = checkUserSignInStatusUseCase()
    }

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

    fun onDeleteNote(note: Note) {
        viewModelScope.launch {
            note.id?.let { deleteNoteByIdUseCase(it) }
        }
        _selectedNote.value = NoteSelectedStatus.None
    }

    fun onSelectNote(note: Note) {
        _selectedNote.value = NoteSelectedStatus.Selected(note)
    }

    fun onDismissNote() {
        _selectedNote.value = NoteSelectedStatus.None
    }

    sealed interface NoteSelectedStatus {
        data object None : NoteSelectedStatus
        data class Selected(var note: Note) : NoteSelectedStatus
    }
}
