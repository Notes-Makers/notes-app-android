package com.notesmakers.database.domain

import com.notesmakers.database.data.DatabaseDomainImpl
import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.models.DomainNoteModel
import kotlinx.coroutines.flow.Flow

interface DatabaseDomain<Note> {
    val notesDao: NotesDao
    suspend fun insertNote(note: Note): Note
    fun getNotes(): Flow<List<Note>>
    suspend fun deleteNote(id: String)
    suspend fun updateNote(note: Note): Note?

    companion object {
        fun <Note> createDatabaseDomain(
            noteTransformer: (DomainNoteModel) -> Note,
            toDataNoteTransformer: (Note) -> DomainNoteModel
        ): DatabaseDomain<Note> = DatabaseDomainImpl(
            noteTransformer = noteTransformer,
            toDataNoteTransformer = toDataNoteTransformer
        )
    }
}