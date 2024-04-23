package com.notesmakers.noteapp.di

import com.notesmakers.database.data.models.DomainNoteModel
import com.notesmakers.noteapp.extension.localDateFromTimeStamp
import com.notesmakers.noteapp.data.notes.BitmapDrawable
import com.notesmakers.noteapp.data.notes.Note
import com.notesmakers.noteapp.data.notes.PathDrawable
import com.notesmakers.noteapp.data.notes.TextDrawable
import com.notesmakers.noteapp.data.notes.TextNote

fun DomainNoteModel.toNote() = Note(
    id = id,
    title = title,
    description = description,
    ownerId = ownerId,
    pageCount = pageCount,
    noteType = noteType,
    createdAt = createdAt.localDateFromTimeStamp(),
    textNote = textQuickNote?.let {
        TextNote(
            id = it.id,
            text = it.text
        )
    },
    bitmapDrawables = bitmapDrawable.map {
        BitmapDrawable(
            id = it.id,
            width = it.width,
            height = it.height,
            scale = it.scale,
            offsetX = it.offsetX,
            offsetY = it.offsetY,
            bitmap = it.bitmap,
            createdAt = it.createdAt,
            pageNumber = it.notePageIndex,
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
            createdAt = it.createdAt,
            pageNumber = it.notePageIndex,
        )
    },
    textDrawables = textDrawables.map {
        TextDrawable(
            id = it.id,
            text = it.text,
            color = it.color,
            offsetX = it.offsetX,
            offsetY = it.offsetY,
            createdAt = it.createdAt,
            pageNumber = it.notePageIndex,
        )
    },
)
