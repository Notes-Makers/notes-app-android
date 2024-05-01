package com.notesmakers.network.data

import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetItemsInfo
import com.notesmakers.network.data.api.ApiGetNote
import com.notesmakers.network.data.api.ApiGetNotesInfo
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiGetPagesInfo
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.domain.NetworkDomain
import com.notesmakers.network.type.ItemType
import org.koin.java.KoinJavaComponent

class NetworkDomainImpl<Note, NotesInfo, Item, ItemsInfo, Page, PagesInfo>(
    private val noteTransformer: (ApiGetNote) -> Note,
    private val notesInfoTransformer: (ApiGetNotesInfo) -> NotesInfo,
    private val itemTransformer: (ApiGetItem) -> Item,
    private val itemsInfoTransformer: (ApiGetItemsInfo) -> ItemsInfo,
    private val pageTransformer: (ApiGetPage) -> Page,
    private val pagesInfoTransformer: (ApiGetPagesInfo) -> PagesInfo,
) :
    NetworkDomain<Note, NotesInfo, Item, ItemsInfo, Page, PagesInfo> {

    override val networkClient: ApolloNetworkClient by KoinJavaComponent.getKoin()
        .inject<ApolloNetworkClient>()

    override suspend fun addItem(
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
    ): Boolean = runCatching {
        networkClient.addItem(
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
        ).data?.addItem!!
    }.getOrDefault(false)


    override suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String
    ): Boolean = runCatching {
        networkClient.addPage(
            noteId = noteId,
            pageId = pageId,
            createdAt = createdAt,
            createdBy = createdBy,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy
        ).data?.addPage!!
    }.getOrDefault(false)

    override suspend fun addNote(
        name: String,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String
    ): String = runCatching {
        networkClient.addNote(
            name = name,
            description = description,
            createdBy = createdBy,
            isShared = isShared,
            createdAt = createdAt,
            isPrivate = isPrivate,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy
        ).data?.addNote?.noteId!!
    }.getOrElse { exception ->
        throw CreateNoteException(exception.message, exception)
    }

    override suspend fun deleteItem(noteId: String, pageId: String, itemId: String): Boolean =
        runCatching {
            networkClient.deleteItem(
                noteId = noteId, pageId = pageId, itemId = itemId
            ).data?.deleteItem!!
        }.getOrDefault(false)


    override suspend fun deleteNote(noteId: String): Boolean = runCatching {
        networkClient.deleteNote(
            noteId = noteId
        ).data?.deleteNote!!
    }.getOrDefault(false)


    override suspend fun deletePage(noteId: String, pageId: String): Boolean = runCatching {
        networkClient.deletePage(
            noteId = noteId,
            pageId = pageId
        ).data?.deletePage!!
    }.getOrDefault(false)

    override suspend fun getItem(noteId: String, pageId: String, itemId: String): Item =
        itemTransformer(runCatching {
            networkClient.getItem(
                noteId = noteId, pageId = pageId, itemId = itemId
            ).data?.getItem!!
        }.getOrElse { throw GetItemException(it.message, it) }.toApiGetItem())

    override suspend fun getItemsInfo(noteId: String, pageId: String): List<ItemsInfo> =
        runCatching {
            networkClient.getItemsInfo(
                noteId = noteId, pageId = pageId
            ).data?.getItemsInfo!!
        }.getOrElse { throw GetItemsInfoException(it.message, it) }
            .mapNotNull { it?.toApiGetItemsInfo() }.map(itemsInfoTransformer)


    override suspend fun getNote(noteId: String): Note =
        noteTransformer(runCatching {
            networkClient.getNote(
                noteId = noteId,
            ).data?.getNote!!
        }.getOrElse { throw GetNoteException(it.message, it) }.toApiGetNote())


    override suspend fun getNotesInfo(): List<NotesInfo> =
        runCatching {
            networkClient.getNotesInfo(
            ).data?.getNotesInfo!!
        }.getOrElse { throw GetNoteException(it.message, it) }
            .mapNotNull { it?.toApiGetNotesInfo() }.map(notesInfoTransformer)


    override suspend fun getPage(noteId: String, pageId: String): Page =
        pageTransformer(runCatching {
            networkClient.getPage(
                noteId = noteId, pageId = pageId
            ).data?.getPage!!
        }.getOrElse { throw GetNoteException(it.message, it) }.toApiGetPage())


    override suspend fun getPagesInfo(noteId: String): List<PagesInfo> =
        runCatching {
            networkClient.getPagesInfo(
                noteId = noteId
            ).data?.getPagesInfo!!
        }.getOrElse { throw GetNoteException(it.message, it) }
            .mapNotNull { it?.toApiGetPagesInfo() }.map(pagesInfoTransformer)

}