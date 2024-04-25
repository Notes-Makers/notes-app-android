package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class DeleteNoteByIdUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(noteId: String) = databaseDomainModule.deleteNote(noteId = noteId)
}
