package com.notesmakers.noteapp.domain.notes

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class UpdatePinnedStatusUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(
        noteId: String,
        isPinned: Boolean,
    ) = databaseDomainModule.updatePinned(noteId = noteId, isPinned = isPinned)
}
