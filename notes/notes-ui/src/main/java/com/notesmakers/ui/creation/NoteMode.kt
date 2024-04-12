package com.notesmakers.ui.creation

import com.notesmakers.ui.R

enum class NoteMode {
    QUICK_NOTE, PAINT_NOTE
}

val NoteMode.icon
    get() = when (this) {
        NoteMode.QUICK_NOTE -> R.drawable.note
        NoteMode.PAINT_NOTE -> R.drawable.paint_net
    }
val NoteMode.title
    get() = when (this) {
        NoteMode.QUICK_NOTE -> "Quick Note"
        NoteMode.PAINT_NOTE -> "Paint Note"
    }
val NoteMode.desc
    get() = when (this) {
        NoteMode.QUICK_NOTE -> "Here, in this mode, you can write simple formatted text using Markdown conventions."
        NoteMode.PAINT_NOTE -> "Here, in this mode, you can draw on the canvas, create lines, add text, and insert images."
    }