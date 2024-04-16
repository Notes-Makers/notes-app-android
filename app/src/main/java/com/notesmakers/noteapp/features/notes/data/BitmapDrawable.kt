package com.notesmakers.noteapp.features.notes.data

import com.notesmakers.database.data.models.BitmapDrawableModel
import java.util.UUID

data class BitmapDrawable(
    var id: String = UUID.randomUUID().toString(),
    var width: Int,
    var height: Int,
    var scale: Float,
    var offsetX: Float,
    var offsetY: Float,
    val bitmap: String,
    override val timestamp: Long,
) : Drawable {
    fun toBitmapDrawableModel(): BitmapDrawableModel = BitmapDrawableModel(
        id = id,
        width = width,
        height = height,
        scale = scale,
        offsetX = offsetX,
        offsetY = offsetY,
        bitmap = bitmap,
        timestamp = timestamp
    )
}
