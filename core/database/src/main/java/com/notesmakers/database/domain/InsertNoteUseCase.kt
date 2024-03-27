package com.notesmakers.database.domain

import com.notesmakers.database.data.models.Note
import org.koin.core.annotation.Factory

@Factory
class InsertNoteUseCase(
    private val notesDatabaseActions: NotesDatabaseActions
) {
    suspend operator fun invoke(note: Note) = notesDatabaseActions.insertNote(note = note)
}