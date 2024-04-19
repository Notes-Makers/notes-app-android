package com.notesmakers.noteapp.features.notes.domain

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class AddTextDrawableToNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
        notePageIndex: Int,
    ) = databaseDomainModule.addTextDrawableToNote(
        noteId = noteId,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
        notePageIndex = notePageIndex,
    )
}