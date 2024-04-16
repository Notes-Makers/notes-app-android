package com.notesmakers.database.data.models

import com.notesmakers.database.data.entities.RealmTextDrawable
import java.util.UUID

data class TextDrawableModel(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
    var color: Long,
    var offsetX: Float,
    var offsetY: Float,
    var timestamp: Long,
) : DrawableModel<RealmTextDrawable> {
    override fun toRealmDrawableComponent() = RealmTextDrawable(
        id = id,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
        timestamp = timestamp,
    )
}
