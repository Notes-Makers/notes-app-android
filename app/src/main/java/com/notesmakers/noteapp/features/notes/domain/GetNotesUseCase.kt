package com.notesmakers.noteapp.features.notes.domain

import com.notesmakers.noteapp.di.DatabaseDomainModule
import org.koin.core.annotation.Factory

@Factory
class GetNotesUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    operator fun invoke() = databaseDomainModule.getNotes()
}
