package com.notesmakers.database.data.models

import java.util.UUID

data class PathDrawableModel(
    var id: String = UUID.randomUUID().toString(),
    var remoteItemId: String?,
    var strokeWidth: Float,
    var color: String,
    var alpha: Float,
    var eraseMode: Boolean,
    var path: String,
    var createdAt: Long,
)

