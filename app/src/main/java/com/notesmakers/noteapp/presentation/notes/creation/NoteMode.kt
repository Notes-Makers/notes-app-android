package com.notesmakers.noteapp.presentation.notes.creation

import com.notesmakers.noteapp.data.notes.NoteDrawableType
import com.notesmakers.noteapp.presentation.notes.creation.NoteMode.PAINT_NOTE
import com.notesmakers.noteapp.presentation.notes.creation.NoteMode.QUICK_NOTE

enum class NoteMode {
    QUICK_NOTE, PAINT_NOTE
}

fun NoteMode.toNoteType() = when (this) {
    QUICK_NOTE -> NoteDrawableType.QUICK_NOTE.type
    PAINT_NOTE -> NoteDrawableType.PAINT_NOTE.type
}

val NoteMode.icon
    get() = when (this) {
        QUICK_NOTE -> com.notesmakers.common_ui.R.drawable.note
        PAINT_NOTE -> com.notesmakers.common_ui.R.drawable.paint_net
    }
val NoteMode.title
    get() = when (this) {
        QUICK_NOTE -> "Quick Note"
        PAINT_NOTE -> "Paint Note"
    }
val NoteMode.desc
    get() = when (this) {
        QUICK_NOTE -> "Here, in this mode, you can write simple formatted text using Markdown conventions."
        PAINT_NOTE -> "Here, in this mode, you can draw on the canvas, create lines, add text, and insert images."
    }