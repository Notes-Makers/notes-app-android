package com.notesmakers.database.data

import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.data.models.toNoteData
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
        title: String,
        description: String,
        ownerId: String,
        noteType: String
    ): Note = withContext(Dispatchers.IO) {
        notesDao.createNote(
            title = title,
            description = description,
            ownerId = ownerId,
            noteType = noteType
        ).toNoteData().noteTransformer()
    }

    override suspend fun addTextDrawableToNote(
        noteId: String,
        text: String,
        color: Long,
        offsetX: Float,
        offsetY: Float,
        notePageIndex: Int
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.addTextDrawableToNote(
            noteId = noteId,
            text = text,
            color = color,
            offsetX = offsetX,
            offsetY = offsetY,
            notePageIndex = notePageIndex,
        )?.toNoteData()?.noteTransformer()
    }

    override suspend fun addBitmapDrawableToNote(
        noteId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        notePageIndex: Int
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.addBitmapDrawableToNote(
            noteId = noteId,
            width = width,
            height = height,
            scale = scale,
            offsetX = offsetX,
            offsetY = offsetY,
            bitmap = bitmap,
            notePageIndex = notePageIndex
        )?.toNoteData()?.noteTransformer()
    }

    override suspend fun addPathDrawableToNote(
        noteId: String,
        strokeWidth: Float,
        color: Long,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
        notePageIndex: Int
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.addPathDrawableToNote(
            noteId = noteId,
            strokeWidth = strokeWidth,
            color = color,
            alpha = alpha,
            eraseMode = eraseMode,
            path = path,
            notePageIndex = notePageIndex,
        )?.toNoteData()?.noteTransformer()
    }

    override fun getNotes(): Flow<List<Note>> =
        notesDao.getNotes()
            .map { results -> results.map { it.toNoteData().noteTransformer() }.toList() }

    override suspend fun deleteNote(noteId: String) = withContext(Dispatchers.IO) {
        notesDao.deleteNote(noteId)
    }

    override suspend fun updateNote(
        noteId: String,
        title: String,
        description: String,
        ownerId: String,
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.updateNote(
            noteId = noteId,
            title = title,
            description = description,
            ownerId = ownerId,
        )?.toNoteData()?.noteTransformer()
    }
}