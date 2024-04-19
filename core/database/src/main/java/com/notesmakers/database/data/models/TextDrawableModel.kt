package com.notesmakers.database.data.models

import com.notesmakers.database.data.entities.RealmTextDrawable
import java.util.UUID

data class TextDrawableModel(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
    var color: String,
    var offsetX: Float,
    var offsetY: Float,
    var createdAt: Long,
    var notePageIndex: Int,
) : DrawableModel<RealmTextDrawable> {
    override fun toRealmDrawableComponent() = RealmTextDrawable(
        id = id,
        text = text,
        color = color,
        offsetX = offsetX,
        offsetY = offsetY,
        createdAt = createdAt,
        notePageIndex = notePageIndex,
    )
}
