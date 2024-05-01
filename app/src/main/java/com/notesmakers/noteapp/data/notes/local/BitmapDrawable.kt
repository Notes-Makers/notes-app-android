package com.notesmakers.noteapp.data.notes.local

import java.util.UUID

data class BitmapDrawable(
    var id: String = UUID.randomUUID().toString(),
    var width: Int,
    var height: Int,
    var scale: Float,
    var offsetX: Float,
    var offsetY: Float,
    val bitmap: String,
    val bitmapUrl: String,
    override val createdAt: Long,
    override val remoteId: String?,
) : Drawable
