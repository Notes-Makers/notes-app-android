package com.notesmakers.database.data.models

import java.util.UUID

data class PageOutputModel(
    var id: String = UUID.randomUUID().toString(),
    var remotePageId: String?,
    var createdAt: Long,
    var createdBy: String,
    var modifiedAt: Long,
    var modifiedBy: String,
    var bitmapDrawable: List<BitmapDrawableModel> = listOf(),
    var pathDrawables: List<PathDrawableModel> = listOf(),
    var textDrawables: List<TextDrawableModel> = listOf(),
)