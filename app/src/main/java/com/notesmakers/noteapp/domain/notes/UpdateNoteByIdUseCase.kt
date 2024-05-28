package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class UpdateNoteByIdUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String?,
        name: String? = null,
        description: String? = null,
    ) = databaseDomainModule.updateNote(
        noteId = noteId,
        name = name,
        description = description,
        modifiedAt = null
    )
}

