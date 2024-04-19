package com.notesmakers.noteapp.features.notes.data

import java.util.UUID

data class BitmapDrawable(
    var id: String = UUID.randomUUID().toString(),
    var width: Int,
    var height: Int,
    var scale: Float,
    var offsetX: Float,
    var offsetY: Float,
    val bitmap: String,
    override val createdAt: Long,
    override val pageNumber: Int,
) : Drawable
