package com.notesmakers.database.data.models

import com.notesmakers.database.data.entities.RealmBitmapDrawable
import java.util.UUID

data class BitmapDrawableModel(
    var id: String = UUID.randomUUID().toString(),
    var width: Int,
    var height: Int,
    var scale: Float,
    var offsetX: Float,
    var offsetY: Float,
    val bitmap: String,
    var createdAt: Long,
    var notePageIndex: Int,
) : DrawableModel<RealmBitmapDrawable> {
    override fun toRealmDrawableComponent(): RealmBitmapDrawable = RealmBitmapDrawable(
        id = id,
        width = width,
        height = height,
        scale = scale,
        offsetX = offsetX,
        offsetY = offsetY,
        bitmap = bitmap,
        createdAt = createdAt,
        notePageIndex = notePageIndex
    )
}
