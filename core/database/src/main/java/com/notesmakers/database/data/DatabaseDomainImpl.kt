package com.notesmakers.database.data

import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.domain.DatabaseDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.koin.java.KoinJavaComponent.getKoin

class DatabaseDomainImpl<Note>(
    private val noteTransformer: DomainNoteModel.() -> Note,
) : DatabaseDomain<Note> {
    override val notesDao: NotesDao by getKoin().inject<NotesDao>()
    override suspend fun createNote(
        name: String,
        description: String,
        createdBy: String,
        noteType: String,
    ): Note = withContext(Dispatchers.IO) {
        notesDao.createNote(
            name = name,
            description = description,
            createdBy = createdBy,
            noteType = noteType
        ).toNoteData().noteTransformer()
    }

    override suspend fun addTextDrawableToNote(
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ): Boolean = withContext(Dispatchers.IO) {
        notesDao.addTextDrawableToNote(
            pageId = pageId,
            text = text,
            color = color,
            offsetX = offsetX,
            offsetY = offsetY,
        ) == null
    }

    override suspend fun addBitmapDrawableToNote(
        pageId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        bitmapUrl: String,
    ): Boolean = withContext(Dispatchers.IO) {
        notesDao.addBitmapDrawableToNote(
            pageId = pageId,
            width = width,
            height = height,
            scale = scale,
            offsetX = offsetX,
            offsetY = offsetY,
            bitmap = bitmap,
            bitmapUrl = bitmapUrl,
        ) == null
    }

    override suspend fun addPathDrawableToNote(
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
    ): Boolean = withContext(Dispatchers.IO) {
        notesDao.addPathDrawableToNote(
            pageId = pageId,
            strokeWidth = strokeWidth,
            color = color,
            alpha = alpha,
            eraseMode = eraseMode,
            path = path,
        ) == null
    }

    override fun getNotes(): Flow<List<Note>> =
        notesDao.getNotes()
            .map { results -> results.map { it.toNoteData().noteTransformer() }.toList() }

    override fun getNoteById(noteId: String): Note? =
        notesDao.getNoteById(noteId)?.toNoteData()?.noteTransformer()

    override suspend fun deleteNote(noteId: String) = withContext(Dispatchers.IO) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun updateNote(
        noteId: String?,
        name: String?,
        description: String?,
        modifiedBy: String?,
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.updateNote(
            noteId = noteId,
            name = name,
            description = description,
            modifiedBy = modifiedBy,
        )?.toNoteData()?.noteTransformer()
    }

    override suspend fun updatePageNote(noteId: String, createdBy: String): Note? =
        withContext(Dispatchers.IO) {
            notesDao.updatePageNote(
                noteId = noteId,
                createdBy = createdBy,
            )?.toNoteData()?.noteTransformer()
        }

    override suspend fun updateTextNote(noteId: String, text: String): Note? =
        withContext(Dispatchers.IO) {
            notesDao.updateTextNote(
                noteId = noteId,
                text = text,
            )?.toNoteData()?.noteTransformer()
        }
}