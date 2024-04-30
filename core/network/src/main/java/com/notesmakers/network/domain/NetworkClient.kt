package com.notesmakers.network.domain

import com.apollographql.apollo3.api.ApolloResponse
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
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.type.ItemType

interface NetworkClient {

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
    ): ApolloResponse<AddItemMutation.Data>

    suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String
    ): ApolloResponse<AddPageMutation.Data>

    suspend fun addNote(
        name: String,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String
    ): ApolloResponse<CreateNoteMutation.Data>

    suspend fun deleteItem(
        noteId: String,
        pageId: String,
        itemId: String
    ): ApolloResponse<DeleteItemMutation.Data>

    suspend fun deleteNote(noteId: String): ApolloResponse<DeleteNoteMutation.Data>

    suspend fun deletePage(noteId: String, pageId: String): ApolloResponse<DeletePageMutation.Data>

    suspend fun getItem(
        noteId: String,
        pageId: String,
        itemId: String
    ): ApolloResponse<GetItemQuery.Data>

    suspend fun getItemsInfo(noteId: String, pageId: String): ApolloResponse<GetItemsInfoQuery.Data>
    suspend fun getNote(noteId: String): ApolloResponse<GetNoteQuery.Data>

    suspend fun getNotesInfo(): ApolloResponse<GetNotesInfoQuery.Data>

    suspend fun getPage(noteId: String, pageId: String): ApolloResponse<GetPageQuery.Data>
    suspend fun getPagesInfo(noteId: String): ApolloResponse<GetPagesInfoQuery.Data>
}