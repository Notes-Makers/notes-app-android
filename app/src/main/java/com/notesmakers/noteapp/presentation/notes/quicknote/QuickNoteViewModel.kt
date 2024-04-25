package com.notesmakers.noteapp.presentation.notes.quicknote

import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.domain.notes.GetNoteByIdUseCase
import com.notesmakers.noteapp.domain.notes.UpdateTextNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class QuickNoteViewModel(
    private val noteId: String,
    private val updateTextNoteUseCase: UpdateTextNoteUseCase,
    getNoteByIdUseCase: GetNoteByIdUseCase,
) : BaseViewModel() {
    private val _noteState = MutableStateFlow(getNoteByIdUseCase(noteId))
    val noteState = _noteState.asStateFlow()

    fun updateTextNote(
        noteId: String,
        text: String,
    ) = viewModelScope.launch {
        _noteState.value = updateTextNoteUseCase(noteId = noteId, text = text)
    }
}