package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory
import java.util.UUID

@Factory
class AddTextDrawableToNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        createdAt: Long = System.currentTimeMillis(),
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ) = databaseDomainModule.addTextDrawableToNote(
        id = id,
        createdAt = createdAt,
        pageId = pageId,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
        noteId = noteId
    )
}