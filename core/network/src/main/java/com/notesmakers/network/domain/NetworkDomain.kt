package com.notesmakers.network.domain

import com.notesmakers.network.data.NetworkDomainImpl
import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetItemsInfo
import com.notesmakers.network.data.api.ApiGetNote
import com.notesmakers.network.data.api.ApiGetNotesInfo
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiGetPagesInfo
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiNoteType
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.type.ItemType
import kotlinx.coroutines.withContext
import java.util.UUID

interface NetworkDomain<Note, NotesInfo, Item, ItemsInfo, Page, PagesInfo> {
    val networkClient: NetworkClient
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
    ): Boolean

    suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String,
    ): Boolean

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
    ): String

    suspend fun deleteItem(noteId: String, pageId: String, itemId: String): Boolean

    suspend fun deleteNote(noteId: String): Boolean

    suspend fun deletePage(noteId: String, pageId: String): Boolean


    suspend fun getNote(noteId: String): Note
    suspend fun getNotesInfo(): List<NotesInfo>
    suspend fun getItem(
        noteId: String,
        pageId: String,
        itemId: String
    ): Item

    suspend fun getItemsInfo(noteId: String, pageId: String): List<ItemsInfo>
    suspend fun getPage(noteId: String, pageId: String): Page
    suspend fun getPagesInfo(noteId: String): List<PagesInfo>
    suspend fun updateNote(
        noteId: String,
        name: String,
        description: String,
    )

    suspend fun updateItem(
        noteId: String,
        pageId: String,
        itemId: String,
        text: String,
    )

    companion object {
        fun <Note, NotesInfo, Item, ItemsInfo, Page, PagesInfo> createNetworkDomain(
            noteTransformer: (ApiGetNote) -> Note,
            notesInfoTransformer: (ApiGetNotesInfo) -> NotesInfo,
            itemTransformer: (ApiGetItem) -> Item,
            itemsInfoTransformer: (ApiGetItemsInfo) -> ItemsInfo,
            pageTransformer: (ApiGetPage) -> Page,
            pagesInfoTransformer: (ApiGetPagesInfo) -> PagesInfo,
        ): NetworkDomain<Note, NotesInfo, Item, ItemsInfo, Page, PagesInfo> = NetworkDomainImpl(
            noteTransformer = noteTransformer,
            notesInfoTransformer = notesInfoTransformer,
            itemTransformer = itemTransformer,
            itemsInfoTransformer = itemsInfoTransformer,
            pageTransformer = pageTransformer,
            pagesInfoTransformer = pagesInfoTransformer
        )
    }
}