package com.notesmakers.network.data

import com.apollographql.apollo3.ApolloClient
import com.notesmakers.network.AddItemMutation
import com.notesmakers.network.AddPageMutation
import com.notesmakers.network.CreateNoteMutation
import com.notesmakers.network.DeleteItemMutation
import com.notesmakers.network.DeleteNoteMutation
import com.notesmakers.network.DeletePageMutation
import com.notesmakers.network.GetItemQuery
import com.notesmakers.network.GetItemsInfoQuery
import com.notesmakers.network.GetNoteQuery
import com.notesmakers.network.GetNotesInfoQuery
import com.notesmakers.network.GetPageQuery
import com.notesmakers.network.GetPagesInfoQuery
import com.notesmakers.network.domain.NetworkClient
import com.notesmakers.network.type.ItemType

class ApolloNetworkClient(
    private val apolloClient: ApolloClient,
) : NetworkClient {
    override suspend fun addItem(
        noteId: String,
        pageId: String,
        itemId: String,
        itemType: ItemType,
        itemContent: String,
        itemPosX: Double,
        itemPosY: Double,
        itemWidth: Double,
        itemHeight: Double,
        itemCreatedAt: String,
        itemCreatedBy: String,
        itemModifiedAt: String,
        itemModifiedBy: String,
        itemHash: String
    ) = apolloClient.mutation(
        AddItemMutation(
            noteId = noteId,
            pageId = pageId,
            itemId = itemId,
            itemType = itemType,
            itemContent = itemContent,
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
    ).execute()


    override suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String
    ) = apolloClient.mutation(
        AddPageMutation(
            noteId = noteId,
            pageId = pageId,
            createdAt = createdAt,
            createdBy = createdBy,
            modifiedAt = modifiedAt,
            modifiedBy = modifiedBy
        )
    ).execute()


    override suspend fun addNote(
        name: String,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String
    ) =
        apolloClient.mutation(
            CreateNoteMutation(
                name = name,
                description = description,
                createdBy = createdBy,
                isShared = isShared,
                createdAt = createdAt,
                isPrivate = isPrivate,
                modifiedAt = modifiedAt,
                modifiedBy = modifiedBy
            ),
        ).execute()


    override suspend fun deleteItem(noteId: String, pageId: String, itemId: String) =
        apolloClient.mutation(
            DeleteItemMutation(
                noteId = noteId,
                pageId = pageId,
                itemId = itemId
            ),
        ).execute()


    override suspend fun deleteNote(noteId: String) =
        apolloClient.mutation(
            DeleteNoteMutation(
                noteId = noteId
            )
        ).execute()


    override suspend fun deletePage(noteId: String, pageId: String) =
        apolloClient.mutation(
            DeletePageMutation(
                noteId = noteId,
                pageId = pageId,
            )
        ).execute()


    override suspend fun getItem(noteId: String, pageId: String, itemId: String) =
        apolloClient.query(
            GetItemQuery(
                noteId = noteId,
                pageId = pageId,
                itemId = itemId,
            )
        ).execute()


    override suspend fun getItemsInfo(noteId: String, pageId: String) =
        apolloClient.query(
            GetItemsInfoQuery(
                noteId = noteId,
                pageId = pageId,
            )
        ).execute()


    override suspend fun getNote(noteId: String) =
        apolloClient.query(
            GetNoteQuery(
                noteId = noteId,
            )
        ).execute()


    override suspend fun getNotesInfo() =
        apolloClient.query(
            GetNotesInfoQuery()
        ).execute()


    override suspend fun getPage(noteId: String, pageId: String) =
        apolloClient.query(
            GetPageQuery(
                noteId = noteId,
                pageId = pageId,
            )
        ).execute()


    override suspend fun getPagesInfo(noteId: String) =
        apolloClient.query(
            GetPagesInfoQuery(
                noteId = noteId,
            )
        ).execute()
}
