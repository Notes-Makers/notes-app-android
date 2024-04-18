package com.notesmakers.noteapp.features.notes.data

import java.time.LocalDateTime
import java.util.UUID


data class Note(
    val id: String? = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val ownerId: String,
    val createdAt: LocalDateTime,
    val pageCount: Int,
    val noteType: String,
    var bitmapDrawables: List<BitmapDrawable> = listOf(),
    var pathDrawables: List<PathDrawable> = listOf(),
    var textDrawables: List<TextDrawable> = listOf(),
) {
    val mergedDrawables: List<Drawable> =
        (bitmapDrawables + pathDrawables + textDrawables).sortedBy { it.createdAt }
}
