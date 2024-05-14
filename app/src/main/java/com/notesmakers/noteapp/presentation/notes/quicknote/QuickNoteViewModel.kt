package com.notesmakers.noteapp.presentation.notes.quicknote

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.domain.notes.GetNoteByIdUseCase
import com.notesmakers.noteapp.domain.notes.RewordTextUseCase
import com.notesmakers.noteapp.domain.notes.UpdateTextNoteUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class QuickNoteViewModel(
    private val noteId: String,
    private val updateTextNoteUseCase: UpdateTextNoteUseCase,
    private val rewordTextUseCase: RewordTextUseCase,
    getNoteByIdUseCase: GetNoteByIdUseCase,
) : BaseViewModel() {
    private val _noteState = MutableStateFlow(getNoteByIdUseCase(noteId))
    val noteState = _noteState.asStateFlow()

    private val _aiState = MutableStateFlow<AiState>(AiState.None)
    val aiState = _aiState.asStateFlow()

    fun updateTextNote(
        noteId: String,
        text: String,
    ) = viewModelScope.launch {
        _noteState.value = updateTextNoteUseCase(noteId = noteId, text = text)
    }

    fun rewordText(
        text: String
    ) = viewModelScope.launch {
        runCatching {
            _aiState.value = AiState.Loading
            rewordTextUseCase(text = text)
        }.onSuccess { updateText ->
            updateText?.let {
                sendMessageEvent(MessageEvent.Success)
                _aiState.value = (AiState.Success(it))
            } ?: run {
                sendMessageEvent(MessageEvent.Error("Something gone wrong"))
            }
            delay(1000)
            _aiState.value = AiState.None
        }.onFailure {
            _aiState.value = AiState.None
            sendMessageEvent(MessageEvent.Error("Something gone wrong"))
        }
    }

    sealed interface AiState {
        data object None : AiState
        data object Loading : AiState
        data class Success(val text: String) : AiState
    }

}