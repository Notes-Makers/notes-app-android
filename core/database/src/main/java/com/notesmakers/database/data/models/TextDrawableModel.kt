package com.notesmakers.database.data.models

import java.util.UUID

data class TextDrawableModel(
    var id: String = UUID.randomUUID().toString(),
    var remoteItemId: String?,
    var text: String,
    var color: String,
    var offsetX: Float,
    var offsetY: Float,
    var createdAt: Long,
)