package com.notesmakers.database.domain

import com.notesmakers.database.data.DatabaseDomainImpl
import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.entities.UNDEFINED
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.data.models.PageOutputModel
import com.notesmakers.database.data.models.QuickNoteModel
import kotlinx.coroutines.flow.Flow

interface DatabaseDomain<Note> {
    val notesDao: NotesDao
    suspend fun createNote(
        name: String,
        description: String,
        createdBy: String,
        noteType: String = UNDEFINED,
    ): Note

    suspend fun createCompleteNote(
        remoteNoteId: String,
        name: String,
        description: String,
        noteType: String,
        createdAt: Long,
        createdBy: String = "",
        pages: List<PageOutputModel>,
        modifiedBy: String = createdBy,
        modifiedAt: Long = System.currentTimeMillis(),
        isPrivate: Boolean = false,
        isShared: Boolean = false,
        isPinned: Boolean = false,
        tag: List<String> = listOf(),
        quickNote: QuickNoteModel,
    ): Note

    suspend fun addTextDrawableToNote(
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ): Boolean

    suspend fun addBitmapDrawableToNote(
        pageId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        bitmapUrl: String,
    ): Boolean

    suspend fun addPathDrawableToNote(
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
    ): Boolean

    fun getNotes(): Flow<List<Note>>

    fun getNoteById(noteId: String): Note?

    suspend fun deleteNote(
        noteId: String,
    )

    suspend fun updateNote(
        noteId: String?,
        name: String?,
        description: String?,
        modifiedBy: String?,
    ): Note?

    suspend fun updateRemoteNoteId(
        noteId: String,
        remoteNoteId: String?
    ): Note?

    suspend fun updatePageNote(
        noteId: String,
        createdBy: String
    ): Note?

    suspend fun updateTextNote(
        noteId: String,
        text: String,
    ): Note?

    suspend fun updatePinned(
        noteId: String,
        isPinned: Boolean,
    ): Note?

    companion object {
        fun <Note> createDatabaseDomain(
            noteTransformer: (DomainNoteModel) -> Note,
        ): DatabaseDomain<Note> = DatabaseDomainImpl(
            noteTransformer = noteTransformer,
        )
    }
}