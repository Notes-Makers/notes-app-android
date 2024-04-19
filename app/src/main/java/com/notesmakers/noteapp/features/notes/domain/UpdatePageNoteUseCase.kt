package com.notesmakers.noteapp.features.notes.domain

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class UpdatePageNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String, pageCount: Int
    ) = databaseDomainModule.updatePageNote(
        noteId = noteId,
        pageCount = pageCount,
    )
}