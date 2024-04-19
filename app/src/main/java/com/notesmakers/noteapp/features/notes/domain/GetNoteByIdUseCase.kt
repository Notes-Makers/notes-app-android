package com.notesmakers.noteapp.features.notes.domain

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class GetNoteByIdUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    operator fun invoke(noteId: String) = databaseDomainModule.getNoteById(noteId = noteId)
}
