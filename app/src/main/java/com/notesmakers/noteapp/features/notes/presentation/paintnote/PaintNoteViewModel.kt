package com.notesmakers.noteapp.features.notes.presentation.paintnote

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.features.notes.domain.AddBitmapDrawableToNoteUseCase
import com.notesmakers.noteapp.features.notes.domain.AddPathDrawableToNoteUseCase
import com.notesmakers.noteapp.features.notes.domain.AddTextDrawableToNoteUseCase
import com.notesmakers.noteapp.features.notes.domain.GetNoteByIdUseCase
import com.notesmakers.noteapp.features.notes.domain.UpdatePageNoteUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class PaintNoteViewModel(
    private val noteId: String,
    private val addTextDrawableToNoteUseCase: AddTextDrawableToNoteUseCase,
    private val addBitmapDrawableToNoteUseCase: AddBitmapDrawableToNoteUseCase,
    private val addPathDrawableToNoteUseCase: AddPathDrawableToNoteUseCase,
    private val updatePageNoteUseCase: UpdatePageNoteUseCase,
    getNoteByIdUseCase: GetNoteByIdUseCase,
) : ViewModel() {
    private val _noteState = MutableStateFlow(getNoteByIdUseCase(noteId))
    val noteState = _noteState.asStateFlow()

    fun addTextDrawableToNote(
        text: String,
        color: Long,
        offsetX: Float,
        offsetY: Float,
        notePageIndex: Int,
    ) = viewModelScope.launch {
        addTextDrawableToNoteUseCase(
            noteId = noteId,
            text = text,
            color = color,
            notePageIndex = notePageIndex,
            offsetX = offsetX,
            offsetY = offsetY
        )
    }

    fun addBitmapDrawableToNote(
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        notePageIndex: Int,
    ) = viewModelScope.launch {
        addBitmapDrawableToNoteUseCase(
            noteId = noteId,
            width = width,
            height = height,
            scale = scale,
            offsetX = offsetX,
            offsetY = offsetY,
            bitmap = bitmap,
            notePageIndex = notePageIndex
        )
    }

    fun addPathDrawableToNote(
        noteId: String,
        strokeWidth: Float,
        color: Long,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        notePageIndex: Int,
    ) = viewModelScope.launch {
        addPathDrawableToNoteUseCase(
            noteId = noteId,
            strokeWidth = strokeWidth,
            color = color,
            alpha = alpha,
            eraseMode = eraseMode,
            path = path,
            notePageIndex = notePageIndex,
        )
    }

    fun updatePageCount(
        pageCount: Int,
    ) = viewModelScope.launch {
        _noteState.value = updatePageNoteUseCase(
            noteId = noteId,
            pageCount = pageCount
        )
    }
}