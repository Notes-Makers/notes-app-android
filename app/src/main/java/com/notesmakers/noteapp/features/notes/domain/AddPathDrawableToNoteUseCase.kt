package com.notesmakers.noteapp.features.notes.domain

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class AddPathDrawableToNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String,
        strokeWidth: Float,
        color: Long,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        notePageIndex: Int,
    ) = databaseDomainModule.addPathDrawableToNote(
        noteId = noteId,
        strokeWidth = strokeWidth,
        color = color,
        alpha = alpha,
        eraseMode = eraseMode,
        path = path,
        notePageIndex = notePageIndex,
    )
}