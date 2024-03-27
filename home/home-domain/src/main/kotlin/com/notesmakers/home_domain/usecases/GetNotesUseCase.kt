package com.notesmakers.home_domain.usecases

import com.notesmakers.database.domain.NotesDatabaseActions
import kotlinx.coroutines.CoroutineScope
import org.koin.core.annotation.Factory

@Factory
class GetNotesUseCase(
    private val notesDatabaseActions: NotesDatabaseActions
) {
    suspend operator fun invoke() = notesDatabaseActions.getAllNotes()
}