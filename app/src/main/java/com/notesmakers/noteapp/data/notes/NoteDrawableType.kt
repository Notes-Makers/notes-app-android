package com.notesmakers.noteapp.data.notes


enum class NoteDrawableType(var type: String) {
    QUICK_NOTE("QUICK_NOTE"),
    PAINT_NOTE("PAINT_NOTE"),
    UNDEFINED("UNDEFINED"),
}

fun String.toNoteDrawableType(): NoteDrawableType = when {
    this == NoteDrawableType.QUICK_NOTE.type -> NoteDrawableType.QUICK_NOTE
    this == NoteDrawableType.PAINT_NOTE.type -> NoteDrawableType.PAINT_NOTE
    else -> NoteDrawableType.UNDEFINED
}