package com.notesmakers.noteapp.features.notes.presentation.quicknote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.features.notes.domain.GetNoteByIdUseCase
import com.notesmakers.noteapp.features.notes.domain.UpdateTextNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class QuickNoteViewModel(
    private val noteId: String,
    private val updateTextNoteUseCase: UpdateTextNoteUseCase,
    getNoteByIdUseCase: GetNoteByIdUseCase,
) : ViewModel() {
    private val _noteState = MutableStateFlow(getNoteByIdUseCase(noteId))
    val noteState = _noteState.asStateFlow()

    fun updateTextNote(
        noteId: String,
        text: String,
    ) = viewModelScope.launch {
        _noteState.value = updateTextNoteUseCase(noteId = noteId, text = text)
    }
}