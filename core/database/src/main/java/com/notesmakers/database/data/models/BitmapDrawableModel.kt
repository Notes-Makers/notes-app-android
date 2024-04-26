package com.notesmakers.database.data.models

import java.util.UUID

data class BitmapDrawableModel(
    var id: String = UUID.randomUUID().toString(),
    var width: Int,
    var height: Int,
    var scale: Float,
    var offsetX: Float,
    var offsetY: Float,
    val bitmap: String,
    val bitmapUrl: String,
    var createdAt: Long,
)
