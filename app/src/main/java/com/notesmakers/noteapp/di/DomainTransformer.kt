package com.notesmakers.noteapp.di

import com.notesmakers.database.data.models.BitmapDrawableModel
import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.database.data.models.PathDrawableModel
import com.notesmakers.database.data.models.TextDrawableModel
import com.notesmakers.noteapp.data.notes.BitmapDrawable
import com.notesmakers.noteapp.data.notes.Note
import com.notesmakers.noteapp.data.notes.PageOutput
import com.notesmakers.noteapp.data.notes.PathDrawable
import com.notesmakers.noteapp.data.notes.TextDrawable
import com.notesmakers.noteapp.data.notes.TextNote
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