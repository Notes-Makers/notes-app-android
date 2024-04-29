package com.notesmakers.noteapp.presentation.notes.creation

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.domain.notes.CreateNoteUseCase
import com.notesmakers.noteapp.domain.notes.GetNoteByIdUseCase
import com.notesmakers.noteapp.domain.notes.UpdateNoteByIdUseCase
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NoteCreationViewModel(
    private val noteId: String?,
    private val createNoteUseCase: CreateNoteUseCase,
    private val updateNoteByIdUseCase: UpdateNoteByIdUseCase,
    getNoteByIdUseCase: GetNoteByIdUseCase,
) : BaseViewModel() {
    private val _noteCreationState = MutableStateFlow<NoteCreationState>(NoteCreationState.None)
    val noteCreationState = _noteCreationState.asStateFlow()

    init {
        noteId?.let {
            getNoteByIdUseCase(noteId)?.let { note ->
                _noteCreationState.value = NoteCreationState.Success(note)
            }
        }
    }

    private val _noteCreationEvent = MutableSharedFlow<NoteCreationEvent>()
    val noteCreationEvent = _noteCreationEvent.asSharedFlow()
    fun createNote(
        name: String,
        description: String,
        createdBy: String,
        noteType: String
    ) = viewModelScope.launch {
        runCatching {
            _noteCreationState.value = NoteCreationState.Loading
            createNoteUseCase(
                name = name,
                description = description,
                createdBy = createdBy,
                noteType = noteType
            )
        }.onSuccess {
            _noteCreationState.value = NoteCreationState.Success(it)
            _noteCreationEvent.emit(NoteCreationEvent.NavToNote(it))
        }.onFailure {
            _noteCreationState.value = NoteCreationState.Error(it)
        }
    }

    fun updateNote(
        noteId: String,
        name: String,
        description: String,
    ) {
        viewModelScope.launch {
            runCatching {
                _noteCreationState.value = NoteCreationState.Loading
                updateNoteByIdUseCase(
                    noteId = noteId,
                    name = name,
                    description = description,
                )!!
            }.onSuccess {
                _noteCreationState.value = NoteCreationState.Success(it)
                _noteCreationEvent.emit(NoteCreationEvent.BackEndUpdate)
            }.onFailure {
                _noteCreationState.value = NoteCreationState.Error(it)
            }
        }
    }

    sealed interface NoteCreationEvent {
        data class NavToNote(val note: Note) : NoteCreationEvent
        data object BackEndUpdate : NoteCreationEvent
    }

    sealed interface NoteCreationState {
        data object None : NoteCreationState
        data object Loading : NoteCreationState
        data class Success(val note: Note) : NoteCreationState
        data class Error(val error: Throwable) : NoteCreationState
    }
}