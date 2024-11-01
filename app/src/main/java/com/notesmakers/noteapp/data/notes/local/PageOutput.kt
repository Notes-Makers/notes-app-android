package com.notesmakers.noteapp.data.notes.local

import java.time.ZonedDateTime
import java.util.UUID

data class PageOutput(
    var id: String = UUID.randomUUID().toString(),
    var createdAt: ZonedDateTime,
    var createdBy: String,
    var modifiedAt: ZonedDateTime,
    var modifiedBy: String,
    var bitmapDrawables: List<BitmapDrawable> = listOf(),
    var pathDrawables: List<PathDrawable> = listOf(),
    var textDrawables: List<TextDrawable> = listOf(),
) {
    val mergedDrawables: List<Drawable> =
        (bitmapDrawables + pathDrawables + textDrawables).sortedBy { it.createdAt }
}
