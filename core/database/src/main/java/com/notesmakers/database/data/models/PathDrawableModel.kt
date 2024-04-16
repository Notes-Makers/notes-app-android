package com.notesmakers.database.data.models

import com.notesmakers.database.data.entities.RealmPathDrawable
import java.util.UUID

data class PathDrawableModel(
    var id: String = UUID.randomUUID().toString(),
    var strokeWidth: Float,
    var color: Long,
    var alpha: Float,
    var eraseMode: Boolean,
    var path: String,
    var timestamp: Long,
) : DrawableModel<RealmPathDrawable> {
    override fun toRealmDrawableComponent() = RealmPathDrawable(
        id = id,
        strokeWidth = strokeWidth,
        color = color,
        alpha = alpha,
        eraseMode = eraseMode,
        path = path,
        timestamp = timestamp,
    )
}
