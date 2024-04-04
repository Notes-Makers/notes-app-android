package com.notesmakers.database.data.repository

import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.models.Note
import com.notesmakers.database.domain.NotesDatabaseActions
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class NotesDatabaseActionsImpl(
    private val notesDao: NotesDao
) : NotesDatabaseActions {

    override suspend fun insertNote(note: Note): Note = withContext(Dispatchers.IO) {
        notesDao.insertNote(note)
    }

    override fun getAllNotes(): Flow<ResultsChange<Note>> = notesDao.getAllNotes()

    override suspend fun deleteNote(id: String) = withContext(Dispatchers.IO) {
        notesDao.deleteNote(id)
    }

    override suspend fun updateNote(note: Note) = withContext(Dispatchers.IO) {
        notesDao.updateNote(note)
    }
}