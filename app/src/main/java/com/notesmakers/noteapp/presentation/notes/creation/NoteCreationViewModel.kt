package com.notesmakers.noteapp.presentation.notes.creation

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.data.notes.Note
import com.notesmakers.noteapp.domain.notes.CreateNoteUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NoteCreationViewModel(
    private val createNoteUseCase: CreateNoteUseCase,
) : BaseViewModel() {
    private val _noteCreationState = MutableStateFlow<NoteCreationState>(NoteCreationState.None)
    val noteCreationState = _noteCreationState.asStateFlow()

    private val _noteCreationEvent = MutableSharedFlow<NoteCreationEvent>()
    val noteCreationEvent = _noteCreationEvent.asSharedFlow()
    fun createNote(
        title: String,
        description: String,
        ownerId: String,
        noteType: String
    ) = viewModelScope.launch {
        runCatching {
            _noteCreationState.value = NoteCreationState.Loading
            createNoteUseCase(
                title = title,
                description = description,
                ownerId = ownerId,
                noteType = noteType
            )
        }.onSuccess {
            _noteCreationState.value = NoteCreationState.Success(it)
            _noteCreationEvent.emit(NoteCreationEvent.NavToNote(it))
        }.onFailure {
            _noteCreationState.value = NoteCreationState.Error(it)
        }
    }

    sealed interface NoteCreationEvent {
        data class NavToNote(val note: Note) : NoteCreationEvent
    }

    sealed interface NoteCreationState {
        data object None : NoteCreationState
        data object Loading : NoteCreationState
        data class Success(val note: Note) : NoteCreationState
        data class Error(val error: Throwable) : NoteCreationState
    }
}