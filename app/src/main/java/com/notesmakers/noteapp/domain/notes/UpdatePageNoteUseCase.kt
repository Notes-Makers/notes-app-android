package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class UpdatePageNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String, createdBy: String
    ) = databaseDomainModule.updatePageNote(
        noteId = noteId,
        createdBy = createdBy,
    )
}