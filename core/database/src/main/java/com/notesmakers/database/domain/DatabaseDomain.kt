package com.notesmakers.database.domain

import com.notesmakers.database.data.DatabaseDomainImpl
import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.entities.UNDEFINED
import com.notesmakers.database.data.models.DomainNoteModel
import kotlinx.coroutines.flow.Flow

interface DatabaseDomain<Note> {
    val notesDao: NotesDao
    suspend fun createNote(
        title: String,
        description: String,
        ownerId: String,
        noteType: String = UNDEFINED,
    ): Note

    suspend fun addTextDrawableToNote(
        noteId: String,
        text: String,
        color: Long,
        offsetX: Float,
        offsetY: Float,
        notePageIndex: Int,
    ): Note?

    suspend fun addBitmapDrawableToNote(
        noteId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        notePageIndex: Int,
    ): Note?

    suspend fun addPathDrawableToNote(
        noteId: String,
        strokeWidth: Float,
        color: Long,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        notePageIndex: Int,
    ): Note?

    fun getNotes(): Flow<List<Note>>

    suspend fun deleteNote(
        noteId: String,
    )

    suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        ownerId: String,
    ): Note?

    companion object {
        fun <Note> createDatabaseDomain(
            noteTransformer: (DomainNoteModel) -> Note,
        ): DatabaseDomain<Note> = DatabaseDomainImpl(
            noteTransformer = noteTransformer,
        )
    }
}