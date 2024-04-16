package com.notesmakers.noteapp.features.notes.data

import java.util.UUID


data class Note(
    val id: String? = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    var bitmapDrawables: List<BitmapDrawable> = listOf(),
    var pathDrawables: List<PathDrawable> = listOf(),
    var textDrawables: List<TextDrawable> = listOf(),
) {
    val mergedDrawables: List<Drawable> =
        (bitmapDrawables + pathDrawables + textDrawables).sortedBy { it.timestamp }
}