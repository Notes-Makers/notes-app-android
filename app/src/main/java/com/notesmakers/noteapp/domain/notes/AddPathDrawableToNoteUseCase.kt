package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory
import java.util.UUID

@Factory
class AddPathDrawableToNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        createdAt: Long = System.currentTimeMillis(),
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
    ) = databaseDomainModule.addPathDrawableToNote(
        id = id,
        createdAt = createdAt,
        pageId = pageId,
        strokeWidth = strokeWidth,
        color = color,
        alpha = alpha,
        eraseMode = eraseMode,
        path = path,
        noteId = noteId
    )
}