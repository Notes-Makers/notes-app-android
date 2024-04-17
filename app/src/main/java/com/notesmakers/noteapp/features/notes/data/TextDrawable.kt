package com.notesmakers.noteapp.features.notes.data

import java.util.UUID

data class TextDrawable(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
    var color: Long,
    var offsetX: Float,
    var offsetY: Float,
    override val createdAt: Long,
) : Drawable