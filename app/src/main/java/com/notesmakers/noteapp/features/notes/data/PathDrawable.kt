package com.notesmakers.noteapp.features.notes.data

import java.util.UUID

data class PathDrawable(
    var id: String = UUID.randomUUID().toString(),
    var strokeWidth: Float,
    var color: Long,
    var alpha: Float,
    var eraseMode: Boolean,
    var path: String,
    override val createdAt: Long,
) : Drawable
