package com.notesmakers.noteapp.features.notes.data

import com.notesmakers.database.data.models.PathDrawableModel
import java.util.UUID

data class PathDrawable(
    var id: String = UUID.randomUUID().toString(),
    var strokeWidth: Float,
    var color: Long,
    var alpha: Float,
    var eraseMode: Boolean,
    var path: String,
    override val timestamp: Long,
) : Drawable {
    fun toPathDrawableModel(): PathDrawableModel = PathDrawableModel(
        id = id,
        strokeWidth = strokeWidth,
        color = color,
        alpha = alpha,
        eraseMode = eraseMode,
        path = path,
        timestamp = timestamp,
    )
}
