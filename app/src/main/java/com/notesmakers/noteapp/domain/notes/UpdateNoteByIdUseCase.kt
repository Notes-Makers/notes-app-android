package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class UpdateNoteByIdUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String?,
        title: String?=null,
        description: String?=null,
        ownerId: String?=null,
    ) = databaseDomainModule.updateNote(
        noteId = noteId,
        title = title,
        description = description,
        ownerId = ownerId
    )
}

