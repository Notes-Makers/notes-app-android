package com.notesmakers.noteapp.presentation.notes.paintnote

import androidx.compose.ui.graphics.Path
import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.domain.auth.GetOwnerUseCase
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
    private val getOwnerUseCase: GetOwnerUseCase,
    getNoteByIdUseCase: GetNoteByIdUseCase,
) : BaseViewModel() {
    private val _noteState = MutableStateFlow(getNoteByIdUseCase(noteId))
    val noteState = _noteState.asStateFlow()

    fun addTextDrawableToNote(
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ) = viewModelScope.launch {
        addTextDrawableToNoteUseCase(
            pageId = pageId,
            text = text,
            color = color,
            offsetX = offsetX,
            offsetY = offsetY,
            noteId = noteId
        )
    }

    fun addBitmapDrawableToNote(
        pageId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        bitmapUrl: String,
    ) = viewModelScope.launch {
        addBitmapDrawableToNoteUseCase(
            pageId = pageId,
            width = width,
            height = height,
            scale = scale,
            offsetX = offsetX,
            offsetY = offsetY,
            bitmap = bitmap,
            bitmapUrl = bitmapUrl,
            noteId = noteId
        )
    }

    fun addPathDrawableToNote(
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: Path,
    ) = viewModelScope.launch {
        runCatching {
            addPathDrawableToNoteUseCase(
                pageId = pageId,
                strokeWidth = strokeWidth,
                color = color,
                alpha = alpha,
                eraseMode = eraseMode,
                path = path.getSvgPath(),
                noteId = noteId
            )
        }
    }

    fun updatePageCount(
    ) = viewModelScope.launch {
        _noteState.value = updatePageNoteUseCase(
            noteId = noteId,
            createdBy = getOwnerUseCase()
        )
    }
}