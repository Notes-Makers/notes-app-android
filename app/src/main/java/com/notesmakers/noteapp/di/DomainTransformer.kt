package com.notesmakers.noteapp.di

import com.notesmakers.database.data.models.BitmapDrawableModel
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.data.models.PathDrawableModel
import com.notesmakers.database.data.models.TextDrawableModel
import com.notesmakers.network.data.api.ApiGetItem
import com.notesmakers.network.data.api.ApiGetItemsInfo
import com.notesmakers.network.data.api.ApiGetNote
import com.notesmakers.network.data.api.ApiGetNotesInfo
import com.notesmakers.network.data.api.ApiGetPage
import com.notesmakers.network.data.api.ApiGetPagesInfo
import com.notesmakers.noteapp.data.notes.api.BaseContent
import com.notesmakers.noteapp.data.notes.api.BaseImg
import com.notesmakers.noteapp.data.notes.api.BaseItem
import com.notesmakers.noteapp.data.notes.api.BaseItemsInfo
import com.notesmakers.noteapp.data.notes.api.BaseNote
import com.notesmakers.noteapp.data.notes.api.BaseNotesInfo
import com.notesmakers.noteapp.data.notes.api.BasePage
import com.notesmakers.noteapp.data.notes.api.BasePagesInfo
import com.notesmakers.noteapp.data.notes.api.BasePath
import com.notesmakers.noteapp.data.notes.api.BaseText
import com.notesmakers.noteapp.data.notes.local.BitmapDrawable
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.data.notes.local.PageOutput
import com.notesmakers.noteapp.data.notes.local.PathDrawable
import com.notesmakers.noteapp.data.notes.local.TextDrawable
import com.notesmakers.noteapp.data.notes.local.TextNote
import com.notesmakers.noteapp.extension.localDateFromTimeStamp

fun DomainNoteModel.toNote() = Note(
    id = id,
    name = name,
    description = description,
    createdAt = createdAt.localDateFromTimeStamp(),
    modifiedAt = modifiedAt.localDateFromTimeStamp(),
    createdBy = createdBy,
    modifiedBy = modifiedBy,
    noteType = noteType,
    isPrivate = isPrivate,
    isShared = isShared,
    isPinned = isPinned,
    tag = tag,
    textNote = quickNoteModel?.let { note ->
        TextNote(
            id = note.id,
            text = note.text
        )
    },
    pages = pages.map { page ->
        PageOutput(
            id = page.id,
            createdAt = page.createdAt.localDateFromTimeStamp(),
            createdBy = page.createdBy,
            modifiedAt = page.modifiedAt.localDateFromTimeStamp(),
            modifiedBy = page.modifiedBy,
            bitmapDrawables = page.bitmapDrawable.map { it.toBitmapDrawable() },
            pathDrawables = page.pathDrawables.map { it.toPathDrawable() },
            textDrawables = page.textDrawables.map { it.toTextDrawable() },
        )

    },
)

fun BitmapDrawableModel.toBitmapDrawable() = BitmapDrawable(
    id = id,
    width = width,
    height = height,
    scale = scale,
    offsetX = offsetX,
    offsetY = offsetY,
    bitmap = bitmap,
    createdAt = createdAt,
    bitmapUrl = bitmapUrl,
)

fun PathDrawableModel.toPathDrawable() = PathDrawable(
    id = id,
    strokeWidth = strokeWidth,
    color = color,
    alpha = alpha,
    eraseMode = eraseMode,
    path = path,
    createdAt = createdAt,
)

fun TextDrawableModel.toTextDrawable() = TextDrawable(
    id = id,
    text = text,
    color = color,
    offsetX = offsetX,
    offsetY = offsetY,
    createdAt = createdAt,
)


fun ApiGetNote.toBaseNote() = BaseNote(
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

fun ApiGetNotesInfo.toBaseNotesInfo() = BaseNotesInfo(
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

fun ApiGetItem.toBaseItem() = BaseItem(
    id = id,
    type = type,
    isDeleted = isDeleted,
    content = BaseContent(
        typename = content?.typename,
        onPathOutputType = BasePath(
            strokeWidth = content?.onPathOutputType?.strokeWidth,
            color = content?.onPathOutputType?.color,
            alpha = content?.onPathOutputType?.alpha,
            eraseMode = content?.onPathOutputType?.eraseMode,
            path = content?.onPathOutputType?.path
        ),
        onImgOutputType = BaseImg(
            itemId = content?.onImgOutputType?.itemId,
            noteId = content?.onImgOutputType?.noteId,
        ),
        onTextOutputType = BaseText(
            text = content?.onTextOutputType?.text,
            color = content?.onTextOutputType?.color
        )
    ),
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    hash = hash
)

fun ApiGetItemsInfo.toBaseItemsInfo() = BaseItemsInfo(
    id = id, isDeleted = isDeleted, modifiedAt = modifiedAt, hash = hash
)

fun ApiGetPage.toBasePage() = BasePage(
    id = id,
    isDeleted = isDeleted,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    items = items
)

fun ApiGetPagesInfo.toBasePagesInfo() = BasePagesInfo(
    id = id,
    createdAt = createdAt,
    createdBy = createdBy,
    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy,
    isDeleted = isDeleted,
    itemSize = itemSize
)
