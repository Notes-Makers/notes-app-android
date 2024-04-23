package com.notesmakers.noteapp.presentation.notes.paintnote

import androidx.compose.ui.graphics.Path
import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.extension.getSvgPath
import com.notesmakers.noteapp.domain.notes.AddBitmapDrawableToNoteUseCase
import com.notesmakers.noteapp.domain.notes.AddPathDrawableToNoteUseCase
import com.notesmakers.noteapp.domain.notes.AddTextDrawableToNoteUseCase
import com.notesmakers.noteapp.domain.notes.GetNoteByIdUseCase
import com.notesmakers.noteapp.domain.notes.UpdatePageNoteUseCase
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
) : BaseViewModel() {
    private val _noteState = MutableStateFlow(getNoteByIdUseCase(noteId))
    val noteState = _noteState.asStateFlow()

    fun addTextDrawableToNote(
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
        notePageIndex: Int,
    ) = viewModelScope.launch {
        _noteState.value = addTextDrawableToNoteUseCase(
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
        _noteState.value = addBitmapDrawableToNoteUseCase(
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
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: Path,
        notePageIndex: Int,
    ) = viewModelScope.launch {
        runCatching {
            addPathDrawableToNoteUseCase(
                noteId = noteId,
                strokeWidth = strokeWidth,
                color = color,
                alpha = alpha,
                eraseMode = eraseMode,
                path = path.getSvgPath(),
                notePageIndex = notePageIndex,
            )
        }.onSuccess {
            _noteState.value = it
        }
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