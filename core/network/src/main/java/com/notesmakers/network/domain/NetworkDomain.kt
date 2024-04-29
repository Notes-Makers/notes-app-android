package com.notesmakers.network.domain

import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetItemsInfo
import com.notesmakers.network.data.api.ApiGetNote
import com.notesmakers.network.data.api.ApiGetNotesInfo
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiGetPagesInfo
import com.notesmakers.network.type.ItemType

interface NetworkDomain {
    suspend fun addItem(
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
    ): Boolean

    suspend fun addPage(
        noteId: String,
        pageId: String,
        createdAt: String,
        createdBy: String,
        modifiedAt: String,
        modifiedBy: String
    ): Boolean

    suspend fun addNote(
        name: String,
        description: String,
        createdBy: String,
        isShared: Boolean,
        createdAt: String,
        isPrivate: Boolean,
        modifiedAt: String,
        modifiedBy: String
    ): String

    suspend fun deleteItem(noteId: String, pageId: String, itemId: String): Boolean

    suspend fun deleteNote(noteId: String): Boolean

    suspend fun deletePage(noteId: String, pageId: String): Boolean

    suspend fun getItem(
        noteId: String,
        pageId: String,
        itemId: String
    ): ApiGetItem

    suspend fun getItemsInfo(noteId: String, pageId: String): List<ApiGetItemsInfo>
    suspend fun getNote(noteId: String): ApiGetNote

    suspend fun getNotesInfo(): List<ApiGetNotesInfo>

    suspend fun getPage(noteId: String, pageId: String): ApiGetPage
    suspend fun getPagesInfo(noteId: String): List<ApiGetPagesInfo>
}