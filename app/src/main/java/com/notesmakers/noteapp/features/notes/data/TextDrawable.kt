package com.notesmakers.noteapp.features.notes.data

import com.notesmakers.database.data.models.TextDrawableModel
import java.util.UUID

data class TextDrawable(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
    var color: Long,
    var offsetX: Float,
    var offsetY: Float,
    override val timestamp: Long,
) : Drawable {
    fun toTextDrawableModel(): TextDrawableModel = TextDrawableModel(
        id = id,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
        timestamp = timestamp,
    )
}
