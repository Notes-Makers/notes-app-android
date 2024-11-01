package com.notesmakers.network.data

import com.notesmakers.network.GetItemQuery
import com.notesmakers.network.GetItemsInfoQuery
import com.notesmakers.network.GetNoteQuery
import com.notesmakers.network.GetNotesInfoQuery
import com.notesmakers.network.GetPageQuery
import com.notesmakers.network.GetPagesInfoQuery
import com.notesmakers.network.data.api.ApiContent
import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetItemsInfo
import com.notesmakers.network.data.api.ApiGetNote
import com.notesmakers.network.data.api.ApiGetNotesInfo
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiGetPagesInfo
import com.notesmakers.network.data.api.ApiImg
import com.notesmakers.network.data.api.ApiNoteType
import com.notesmakers.network.data.api.ApiPath
import com.notesmakers.network.data.api.ApiPosition
import com.notesmakers.network.data.api.ApiText
import com.notesmakers.network.type.NoteType

fun GetItemQuery.GetItem.toApiGetItem() = ApiGetItem(
    id = id,
    type = type,
    isDeleted = isDeleted,
    content = ApiContent(
        typename = content?.__typename,
        onTextOutputType = ApiText(
            text = content?.onTextOutputType?.text,
            color = content?.onTextOutputType?.color,
        ),
        onImgOutputType = ApiImg(
            noteId = content?.onImgOutputType?.noteId,
            scale = content?.onImgOutputType?.scale?.toFloat() ?: 1f,
            itemId = content?.onImgOutputType?.itemId,
        ),
        onPathOutputType = ApiPath(
            strokeWidth = content?.onPathOutputType?.strokeWidth,
            color = content?.onPathOutputType?.color,
            alpha = content?.onPathOutputType?.alpha,
            eraseMode = content?.onPathOutputType?.eraseMode,
            path = content?.onPathOutputType?.path,
        )

    ),
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
    tag = tag,
    type = type?.toApiNoteType(),
    pages = pages?.mapNotNull { page ->
        ApiGetPage(
            id = page?.id,
            isDeleted = page?.isDeleted,
            createdAt = page?.createdAt,
            createdBy = page?.createdBy,
            modifiedAt = page?.modifiedAt,
            modifiedBy = page?.modifiedBy,
            items = page?.items?.mapNotNull {
                it?.toApiItem()
            },
        )
    } ?: emptyList(),
)

fun ApiNoteType.toNoteType() = when (this) {
    ApiNoteType.QUICK -> NoteType.QUICK
    ApiNoteType.PAPER -> NoteType.PAPER
    ApiNoteType.UNKNOWN -> NoteType.UNKNOWN__
}

fun GetNoteQuery.Item.toApiItem() = ApiGetItem(
    id = id,
    type = type,
    isDeleted = isDeleted,
    content = ApiContent(
        typename = content?.__typename,
        onTextOutputType = ApiText(
            text = content?.onTextOutputType?.text,
            color = content?.onTextOutputType?.color,
        ),
        onImgOutputType = ApiImg(
            noteId = content?.onImgOutputType?.noteId,
            scale = content?.onImgOutputType?.scale?.toFloat() ?: 1f,
            itemId = content?.onImgOutputType?.itemId,
        ),
        onPathOutputType = ApiPath(
            strokeWidth = content?.onPathOutputType?.strokeWidth,
            color = content?.onPathOutputType?.color,
            alpha = content?.onPathOutputType?.alpha,
            eraseMode = content?.onPathOutputType?.eraseMode,
            path = content?.onPathOutputType?.path,
        )
    ),
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    hash = hash,
    position = ApiPosition(
        height = position?.height,
        width = position?.width,
        posX = position?.posX,
        posY = position?.posY,
    )
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
    itemSize = itemSize,
    type = type?.toApiNoteType()
)

fun NoteType.toApiNoteType() =
    when (this) {
        NoteType.QUICK -> ApiNoteType.QUICK
        NoteType.PAPER -> ApiNoteType.PAPER
        NoteType.UNKNOWN__ -> ApiNoteType.UNKNOWN
    }


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
    content = ApiContent(
        typename = content?.__typename,
        onTextOutputType = ApiText(
            text = content?.onTextOutputType?.text,
            color = content?.onTextOutputType?.color,
        ),
        onImgOutputType = ApiImg(
            noteId = content?.onImgOutputType?.noteId,
            scale = content?.onImgOutputType?.scale?.toFloat() ?: 1f,
            itemId = content?.onImgOutputType?.itemId,
        ),
        onPathOutputType = ApiPath(
            strokeWidth = content?.onPathOutputType?.strokeWidth,
            color = content?.onPathOutputType?.color,
            alpha = content?.onPathOutputType?.alpha,
            eraseMode = content?.onPathOutputType?.eraseMode,
            path = content?.onPathOutputType?.path,
        )
    ),
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