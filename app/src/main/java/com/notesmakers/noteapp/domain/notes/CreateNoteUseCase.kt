package com.notesmakers.noteapp.domain.notes

import com.notesmakers.database.data.entities.UNDEFINED
import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class CreateNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        createdBy: String,
        noteType: String,
    ) = databaseDomainModule.createNote(
        name = name,
        description = description,
        createdBy = createdBy,
        noteType = noteType
    )
}