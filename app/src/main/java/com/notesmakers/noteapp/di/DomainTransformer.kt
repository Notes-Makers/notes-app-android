package com.notesmakers.noteapp.di

import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.noteapp.features.notes.data.BitmapDrawable
import com.notesmakers.noteapp.features.notes.data.Note
import com.notesmakers.noteapp.features.notes.data.PathDrawable
import com.notesmakers.noteapp.features.notes.data.TextDrawable
import java.util.UUID

fun DomainNoteModel.toNote() = Note(
    id = id,
    title = title,
    description = description,
    bitmapDrawables = bitmapDrawable.map {
        BitmapDrawable(
            id = it.id,
            width = it.width,
            height = it.height,
            scale = it.scale,
            offsetX = it.offsetX,
            offsetY = it.offsetY,
            bitmap = it.bitmap,
            timestamp = it.timestamp,
        )
    },
    pathDrawables = pathDrawables.map {
        PathDrawable(
            id = it.id,
            strokeWidth = it.strokeWidth,
            color = it.color,
            alpha = it.alpha,
            eraseMode = it.eraseMode,
            path = it.path,
            timestamp = it.timestamp,
        )
    },
    textDrawables = textDrawables.map {
        TextDrawable(
            id = it.id,
            text = it.text,
            color = it.color,
            offsetX = it.offsetX,
            offsetY = it.offsetY,
            timestamp = it.timestamp
        )
    },
)

fun Note.toDomainNoteModel() = DomainNoteModel(
    id = id ?: UUID.randomUUID().toString(),
    title = title,
    description = description,
    bitmapDrawable = bitmapDrawables.map { it.toBitmapDrawableModel() },
    pathDrawables = pathDrawables.map { it.toPathDrawableModel() },
    textDrawables = textDrawables.map { it.toTextDrawableModel() },
)


