package com.notesmakers.database.data

import com.notesmakers.database.data.dao.NotesDao
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.data.models.PageOutputModel
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

    override suspend fun createCompleteNote(
        remoteNoteId: String,
        name: String,
        description: String,
        noteType: String,
        createdAt: Long,
        createdBy: String,
        pages: List<PageOutputModel>,
        modifiedBy: String,
        modifiedAt: Long,
        isPrivate: Boolean,
        isShared: Boolean,
        isPinned: Boolean,
        tag: List<String>,
    ): Note = withContext(Dispatchers.IO) {
        notesDao.createCompleteNote(
            remoteNoteId = remoteNoteId,
            name = name,
            description = description,
            noteType = noteType,
            createdAt = createdAt,
            createdBy = createdBy,
            pages = pages,
            modifiedBy = modifiedBy,
            modifiedAt = modifiedAt,
            isPrivate = isPrivate,
            isShared = isShared,
            isPinned = isPinned,
            tag = tag,
        ).toNoteData().noteTransformer()
    }

    override suspend fun addTextDrawableToNote(
        noteId: String,
        id: String,
        createdAt: Long,
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ): Boolean = withContext(Dispatchers.IO) {
        notesDao.addTextDrawableToNote(
            noteId = noteId,
            id = id,
            createdAt = createdAt,
            pageId = pageId,
            text = text,
            color = color,
            offsetX = offsetX,
            offsetY = offsetY,
        ) != null
    }

    override suspend fun addBitmapDrawableToNote(
        noteId: String,
        id: String,
        createdAt: Long,
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
            noteId = noteId,
            id = id,
            createdAt = createdAt,
            pageId = pageId,
            width = width,
            height = height,
            scale = scale,
            offsetX = offsetX,
            offsetY = offsetY,
            bitmap = bitmap,
            bitmapUrl = bitmapUrl,
        ) != null
    }

    override suspend fun addPathDrawableToNote(
        noteId: String,
        id: String,
        createdAt: Long,
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
    ): Boolean = withContext(Dispatchers.IO) {
        notesDao.addPathDrawableToNote(
            noteId = noteId,
            id = id,
            createdAt = createdAt,
            pageId = pageId,
            strokeWidth = strokeWidth,
            color = color,
            alpha = alpha,
            eraseMode = eraseMode,
            path = path,
        ) != null
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
        modifiedAt: Long?,
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.updateNote(
            noteId = noteId,
            name = name,
            description = description,
            modifiedAt = modifiedAt,
        )?.toNoteData()?.noteTransformer()
    }

    override suspend fun updateRemoteNoteId(
        noteId: String,
        remoteNoteId: String?
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.updateRemoteNoteId(
            noteId = noteId,
            remoteNoteId = remoteNoteId,
        )?.toNoteData()?.noteTransformer()
    }

    override suspend fun updatePageNote(noteId: String, createdBy: String): Note? =
        withContext(Dispatchers.IO) {
            notesDao.updatePageNote(
                noteId = noteId,
                createdBy = createdBy,
            )?.toNoteData()?.noteTransformer()
        }

    override suspend fun updateTextNote(noteId: String, text: String, modifiedAt: Long?): String? =
        withContext(Dispatchers.IO) {
            notesDao.updateTextNote(
                noteId = noteId,
                text = text,
                modifiedAt = modifiedAt,
            )
        }

    override suspend fun updatePinned(noteId: String, isPinned: Boolean): Note? =
        withContext(Dispatchers.IO) {
            notesDao.updatePinned(
                noteId = noteId,
                isPinned = isPinned,
            )?.toNoteData()?.noteTransformer()
        }

    //For Sync
    override suspend fun updatePageNote(
        noteId: String,
        pageId: String,
        createdBy: String,
        createdAt: Long,
        modifiedBy: String,
        modifiedAt: Long,
    ): Note? = withContext(Dispatchers.IO) {
        notesDao.updatePageNote(
            noteId = noteId,
            pageId = pageId,
            createdBy = createdBy,
            modifiedBy = modifiedBy,
            createdAt = createdAt,
            modifiedAt = modifiedAt,
        )?.toNoteData()?.noteTransformer()
    }
}