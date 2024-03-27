package com.notesmakers.database.domain

import org.koin.core.annotation.Factory

@Factory
class GetNotesUseCase(
    private val notesDatabaseActions: NotesDatabaseActions
) {
    operator fun invoke() =
        notesDatabaseActions.getAllNotes()
}
