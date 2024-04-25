package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class GetNoteByIdUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    operator fun invoke(noteId: String) = databaseDomainModule.getNoteById(noteId = noteId)
}
