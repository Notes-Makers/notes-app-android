package com.notesmakers.noteapp.data.sync

import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiNoteType
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.type.ItemType
import com.notesmakers.noteapp.data.notes.api.BaseNote
import com.notesmakers.noteapp.data.notes.api.BaseNotesInfo
import com.notesmakers.noteapp.di.NotesNetworkDomainModule
import com.notesmakers.noteapp.domain.notes.GetBitmapUseCase
import com.notesmakers.noteapp.domain.notes.SetBitmapUseCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import org.koin.core.annotation.Factory

@Factory
class NotesRemoteDataSource(
    private val networkDomain: NotesNetworkDomainModule,
    private val ioDispatcher: CoroutineDispatcher,
    val getBitmapUseCase: GetBitmapUseCase,
    val setBitmapUseCase: SetBitmapUseCase,
) {
    suspend fun fetchApiNotes(): List<BaseNotesInfo> =
        withContext(ioDispatcher) {
            networkDomain.getNotesInfo()
        }

    suspend fun getNote(noteId: String): BaseNote = withContext(ioDispatcher) {
        networkDomain.getNote(noteId)
    }

    suspend fun addNote(
        name: String,
        apiNoteType: ApiNoteType,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String,
        pages: List<ApiGetPage>
    ): String = withContext(ioDispatcher) {
        networkDomain.addNote(
            name = name,
            apiNoteType = apiNoteType,
            description = description,
            createdBy = createdBy,
            isShared = isShared,
            createdAt = createdAt,
            isPrivate = isPrivate,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy,
            pages = pages
        )
    }

    suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String
    ) = withContext(ioDispatcher) {
        networkDomain.addPage(
            noteId = noteId,
            pageId = pageId,
            createdAt = createdAt,
            createdBy = createdBy,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy
        )
    }

    suspend fun addItem(
        noteId: String,
        pageId: String,
        itemId: String,
        itemType: ItemType,
        imgContent: ApiImg?,
        pathContent: ApiPath?,
        textContent: ApiText?,
        itemPosX: Double,
        itemPosY: Double,
        itemWidth: Double,
        itemHeight: Double,
        itemCreatedAt: String,
        itemCreatedBy: String,
        itemModifiedAt: String,
        itemModifiedBy: String,
        itemHash: String
    ) = withContext(ioDispatcher) {
        networkDomain.addItem(
            noteId = noteId,
            pageId = pageId,
            itemId = itemId,
            itemType = itemType,
            imgContent = imgContent,
            pathContent = pathContent,
            textContent = textContent,
            itemPosX = itemPosX,
            itemPosY = itemPosY,
            itemWidth = itemWidth,
            itemHeight = itemHeight,
            itemCreatedAt = itemCreatedAt,
            itemCreatedBy = itemCreatedBy,
            itemModifiedAt = itemModifiedAt,
            itemModifiedBy = itemModifiedBy,
            itemHash = itemHash
        )
    }
}