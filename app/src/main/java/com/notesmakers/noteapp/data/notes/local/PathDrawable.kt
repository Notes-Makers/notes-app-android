package com.notesmakers.noteapp.data.notes.local

import java.util.UUID

data class PathDrawable(
    var id: String = UUID.randomUUID().toString(),
    var strokeWidth: Float,
    var color: String,
    var alpha: Float,
    var eraseMode: Boolean,
    var path: String,
    override val createdAt: Long,
    override val remoteId: String?,
) : Drawable
