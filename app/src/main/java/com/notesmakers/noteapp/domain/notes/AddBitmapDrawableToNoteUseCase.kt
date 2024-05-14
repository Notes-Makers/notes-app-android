package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory
import java.util.UUID

@Factory
class AddBitmapDrawableToNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        createdAt: Long = System.currentTimeMillis(),
        pageId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        bitmapUrl: String,
    ) = databaseDomainModule.addBitmapDrawableToNote(
        id = id,
        createdAt = createdAt,
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