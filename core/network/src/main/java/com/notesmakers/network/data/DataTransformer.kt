package com.notesmakers.network.data

import com.notesmakers.network.GetItemQuery
import com.notesmakers.network.GetItemsInfoQuery
import com.notesmakers.network.GetNoteQuery
import com.notesmakers.network.GetNotesInfoQuery
import com.notesmakers.network.GetPageQuery
import com.notesmakers.network.GetPagesInfoQuery
import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetItemsInfo
import com.notesmakers.network.data.api.ApiGetNote
import com.notesmakers.network.data.api.ApiGetNotesInfo
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiGetPagesInfo


fun GetItemQuery.GetItem.toApiGetItem() = ApiGetItem(
    id = id,
    type = type,
    isDeleted = isDeleted,
    content = content,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    hash = hash
)

fun GetItemsInfoQuery.GetItemsInfo.toApiGetItemsInfo() = ApiGetItemsInfo(
    id = id, isDeleted = isDeleted, modifiedAt = modifiedAt, hash = hash
)

fun GetNoteQuery.GetNote.toApiGetNote() = ApiGetNote(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    isPrivate = isPrivate,
    isShared = isShared,
    isDeleted = isDeleted,
    tag = tag
)

fun GetNotesInfoQuery.GetNotesInfo.toApiGetNotesInfo() = ApiGetNotesInfo(
    noteId = noteId,
    name = name,
    description = description,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    isPrivate = isPrivate,
    isShared = isShared,
    isDeleted = isDeleted,
    pageSize = pageSize,
    itemSize = itemSize
)

fun GetPageQuery.GetPage.toApiGetPage() = ApiGetPage(
    id = id,
    isDeleted = isDeleted,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    items = items?.mapNotNull {
        it?.toApiItem()
    }
)

fun GetPageQuery.Item.toApiItem() = ApiGetItem(
    id = id,
    type = type,
    isDeleted = isDeleted,
    content = content,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    hash = hash
)

fun GetPagesInfoQuery.GetPagesInfo.toApiGetPagesInfo() = ApiGetPagesInfo(
    id = id,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    isDeleted = isDeleted,
    itemSize = itemSize
)