package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.NotesNetworkDomainModule
import org.koin.core.annotation.Factory

@Factory
class DeleteNoteUseCase(
    private val networkDomainModule: NotesNetworkDomainModule
) {
    suspend operator fun invoke(noteId: String) = networkDomainModule.deleteNote(noteId = noteId)
}
