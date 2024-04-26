package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class AddTextDrawableToNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ) = databaseDomainModule.addTextDrawableToNote(
        pageId = pageId,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
    )
}