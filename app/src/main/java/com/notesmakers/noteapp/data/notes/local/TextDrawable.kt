package com.notesmakers.noteapp.data.notes.local

import java.util.UUID

data class TextDrawable(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
    var color: String,
    var offsetX: Float,
    var offsetY: Float,
    override val createdAt: Long,
    override val remoteId: String?,
) : Drawable