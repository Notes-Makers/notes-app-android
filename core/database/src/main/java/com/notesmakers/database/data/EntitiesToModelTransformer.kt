package com.notesmakers.database.data

import com.notesmakers.database.data.entities.RealmBitmapDrawable
import com.notesmakers.database.data.entities.RealmNote
import com.notesmakers.database.data.entities.RealmPathDrawable
import com.notesmakers.database.data.entities.RealmTextDrawable
import com.notesmakers.database.data.models.BitmapDrawableModel
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.data.models.PageOutputModel
import com.notesmakers.database.data.models.PathDrawableModel
import com.notesmakers.database.data.models.TextDrawableModel

fun RealmPathDrawable.toPathDrawableModel(): PathDrawableModel = PathDrawableModel(
    id = id,
    strokeWidth = strokeWidth,
    color = color,
    alpha = alpha,
    eraseMode = eraseMode,
    path = path,
    createdAt = createdAt,
)

fun RealmTextDrawable.toTextDrawableModel(): TextDrawableModel = TextDrawableModel(
    id = id,
    text = text,
    color = color,
    offsetX = offsetX,
    offsetY = offsetY,
    createdAt = createdAt,
)

fun RealmBitmapDrawable.toBitmapDrawableModel(): BitmapDrawableModel = BitmapDrawableModel(
    id = id,
    width = width,
    height = height,
    scale = scale,
    offsetX = offsetX,
    offsetY = offsetY,
    bitmap = bitmap,
    bitmapUrl = bitmapUrl,
    createdAt = createdAt,
)

fun RealmNote.toNoteData() = DomainNoteModel(
    id = id,
    remoteNoteId = remoteNoteId,
    name = name,
    description = description,
    noteType = noteType,
    createdAt = createdAt,
    createdBy = createdBy,
    isPrivate = isPrivate,
    isShared = isShared,
    isPinned = isPinned,
    tag = tag,
    pages = pages.map { page ->
        PageOutputModel(
            id = page.id,
            createdAt = page.createdAt,
            createdBy = page.createdBy,
            modifiedAt = page.modifiedAt,
            modifiedBy = page.modifiedBy,
            bitmapDrawable = page.bitmapDrawables.map { it.toBitmapDrawableModel() },
            pathDrawables = page.pathDrawables.map { it.toPathDrawableModel() },
            textDrawables = page.textDrawables.map { it.toTextDrawableModel() },
        )
    },

    modifiedAt = modifiedAt,
    modifiedBy = modifiedBy
)