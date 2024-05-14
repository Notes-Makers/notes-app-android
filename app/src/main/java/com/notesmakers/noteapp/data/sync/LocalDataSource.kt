package com.notesmakers.noteapp.data.sync

import com.notesmakers.database.data.entities.UNDEFINED
import com.notesmakers.database.data.models.PageOutputModel
import com.notesmakers.database.data.models.QuickNoteModel
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.di.DatabaseDomainModule
import com.notesmakers.noteapp.domain.notes.GetBitmapUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.stateIn
import org.koin.core.annotation.Factory
import java.util.UUID


@Factory
class LocalDataSource(
    private val databaseDomainModule: DatabaseDomainModule,
    private val ioDispatcher: CoroutineDispatcher,
) {
    suspend fun fetchDatabaseNotes() =
        databaseDomainModule.getNotes().stateIn(
            CoroutineScope(ioDispatcher)
        )

    suspend fun updateRemoteNoteId(
        noteId: String,
        remoteNoteId: String
    ): Note? = databaseDomainModule.updateRemoteNoteId(noteId, remoteNoteId)

    suspend fun addBitmapItem(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        createdAt: Long = System.currentTimeMillis(),
        pageId: String,
        width: Int,
        height: Int,
        scale: Float,
        offsetX: Float,
        offsetY: Float,
        bitmap: String,
        bitmapUrl: String,
    ) = databaseDomainModule.addBitmapDrawableToNote(
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
        noteId = noteId
    )

    suspend fun addPathItem(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        createdAt: Long = System.currentTimeMillis(),
        pageId: String,
        strokeWidth: Float,
        color: String,
        alpha: Float,
        eraseMode: Boolean,
        path: String,
    ) = databaseDomainModule.addPathDrawableToNote(
        id = id,
        createdAt = createdAt,
        pageId = pageId,
        strokeWidth = strokeWidth,
        color = color,
        alpha = alpha,
        eraseMode = eraseMode,
        path = path,
        noteId = noteId
    )

    suspend fun addTextItem(
        noteId: String,
        id: String = UUID.randomUUID().toString(),
        createdAt: Long = System.currentTimeMillis(),
        pageId: String,
        text: String,
        color: String,
        offsetX: Float,
        offsetY: Float,
    ) = databaseDomainModule.addTextDrawableToNote(
        id = id,
        createdAt = createdAt,
        pageId = pageId,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
        noteId = noteId
    )

    suspend fun addPage(
        noteId: String,
        pageId: String,
        createdBy: String,
        createdAt: Long,
        modifiedBy: String,
        modifiedAt: Long,
    ): Note? =
        databaseDomainModule.updatePageNote(
            noteId = noteId,
            pageId = pageId,
            createdBy = createdBy,
            modifiedBy = modifiedBy,
            createdAt = createdAt,
            modifiedAt = modifiedAt,
        )

    suspend fun addNote(
        remoteNoteId: String,
        name: String,
        description: String,
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
        noteType: String = UNDEFINED,
    ): Note = databaseDomainModule.createCompleteNote(
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
        quickNote = quickNote,
        remoteNoteId = remoteNoteId
    )

}