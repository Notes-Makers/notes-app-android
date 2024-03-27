package com.notesmakers.database.domain

import com.notesmakers.database.data.models.Note
import io.realm.kotlin.notifications.ResultsChange
import kotlinx.coroutines.flow.Flow

interface NotesDatabaseActions {
    suspend fun insertNote(note: Note): Note
    fun getAllNotes(): Flow<ResultsChange<Note>>
    suspend fun deleteNote(id: String)
    suspend fun updateNote(note: Note): Note?
}