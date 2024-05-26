package com.notesmakers.noteapp.presentation.home

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.domain.auth.CheckUserSignInStatusUseCase
import com.notesmakers.noteapp.domain.auth.GetOwnerUseCase
import com.notesmakers.noteapp.domain.auth.LogoutUseCase
import com.notesmakers.noteapp.domain.notes.DeleteNoteByIdUseCase
import com.notesmakers.noteapp.domain.notes.DeleteNoteUseCase
import com.notesmakers.noteapp.domain.notes.GetNotesUseCase
import com.notesmakers.noteapp.domain.notes.UpdatePinnedStatusUseCase
import com.notesmakers.noteapp.domain.sync.NotesSyncRepository
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    val checkUserSignInStatusUseCase: CheckUserSignInStatusUseCase,
    val deleteNoteByIdUseCase: DeleteNoteByIdUseCase,
    val deleteRemote: DeleteNoteUseCase,
    val updatePinnedStatusUseCase: UpdatePinnedStatusUseCase,
    val notesSyncRepository: NotesSyncRepository,
    private val getOwnerUseCase: GetOwnerUseCase,
    val logoutUseCase: LogoutUseCase,
    getNotesUseCase: GetNotesUseCase,
) : BaseViewModel() {
    var notesEventFlow = getNotesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun syncNotes() {
        viewModelScope.launch {
            runCatching {
                notesSyncRepository.syncNotes()
            }.onSuccess {
                sendMessageEvent(MessageEvent.Success)
            }.onFailure { exception ->
                sendMessageEvent(MessageEvent.Error("Something wrong with sync"))
                exception.printStackTrace()
            }
        }
    }

    private val _userIsLoggedIn = MutableStateFlow(checkUserSignInStatusUseCase())
    val userIsLoggedIn = _userIsLoggedIn.asStateFlow()

    private val _selectedNote = MutableStateFlow<NoteSelectedStatus>(NoteSelectedStatus.None)
    val selectedNote = _selectedNote.asStateFlow()


    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    //second state the text typed by the user
    private val _searchText = MutableStateFlow("")
    val searchText = _searchText.asStateFlow()


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
        }
    }

    fun onPinned(note: Note) {
        viewModelScope.launch {
            updatePinnedStatusUseCase(note.id, !note.isPinned)
        }
        _selectedNote.value = NoteSelectedStatus.None
    }

    fun onDeleteNote(note: Note) {
        viewModelScope.launch {
            deleteNoteByIdUseCase(note.id)
            note.remoteId?.let { deleteRemote(it) }
        }
        _selectedNote.value = NoteSelectedStatus.None
    }

    fun onSelectNote(note: Note) {
        _selectedNote.value = NoteSelectedStatus.Selected(note)
    }

    fun onDismissNote() {
        _selectedNote.value = NoteSelectedStatus.None
    }

    val notesList = searchText
        .combine(notesEventFlow) { text, notes ->
            if (text.isBlank()) {
                return@combine notes
            }
            notes.filter { note ->
                note.name.uppercase().contains(text.trim().uppercase())
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),//it will allow the StateFlow survive 5 seconds before it been canceled
            initialValue = notesEventFlow.value
        )

    fun onSearchTextChange(text: String) {
        _searchText.value = text
        if (text.isNotBlank()) {
            _isSearching.value = true
        } else {
            _isSearching.value = false
        }
    }

    fun checkIsUserNoteOrGuest(noteID: String, action: () -> Unit) {
        notesEventFlow.value.find { it.id == noteID }?.let {
            if (it.createdBy == getOwnerUseCase()) {
                action()
            } else {
                sendMessageEvent(MessageEvent.Error("It is not your note"))
            }
        }
    }

    sealed interface NoteSelectedStatus {
        data object None : NoteSelectedStatus
        data class Selected(var note: Note) : NoteSelectedStatus
    }
}
