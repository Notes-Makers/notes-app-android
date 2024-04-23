package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class CreateNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        title: String, description: String, ownerId: String, noteType: String
    ) = databaseDomainModule.createNote(
        title = title, description = description, ownerId = ownerId, noteType = noteType
    )
}