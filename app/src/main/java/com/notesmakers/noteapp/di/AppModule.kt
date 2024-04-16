package com.notesmakers.noteapp.di

import com.notesmakers.database.domain.DatabaseDomain
import com.notesmakers.noteapp.features.notes.data.Note
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton

@Module
@ComponentScan("com.notesmakers.noteapp")
class AppModule {
    @Singleton
    fun createDatabaseDomain(): DatabaseDomain<Note> =
        DatabaseDomain.createDatabaseDomain(
            noteTransformer = {
                it.toNote()
            },
            toDataNoteTransformer = {
                it.toDomainNoteModel()
            }
        )
}

typealias DatabaseDomainModule = DatabaseDomain<Note>