package com.notesmakers.home_domain.usecases

import com.notesmakers.database.data.models.Note
import com.notesmakers.database.domain.NotesDatabaseActions
import org.koin.core.annotation.Factory

@Factory
class InsertNoteUseCase(
    private val notesDatabaseActions: NotesDatabaseActions
) {
    suspend operator fun invoke(note: Note) = notesDatabaseActions.insertNote(note = note)
}