package com.notesmakers.database.data.models

import com.notesmakers.database.data.entities.RealmNote
import java.util.UUID

data class DomainNoteModel(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    val ownerId: String,
    val createdAt: Long,
    var bitmapDrawable: List<BitmapDrawableModel> = listOf(),
    var pathDrawables: List<PathDrawableModel> = listOf(),
    var textDrawables: List<TextDrawableModel> = listOf(),
)

fun RealmNote.toNoteData() = DomainNoteModel(
    id = id,
    title = title,
    description = description,
    ownerId = ownerId,
    createdAt = createdAt,
    bitmapDrawable = bitmapDrawables.map { it.toDrawableComponentModel() },
    pathDrawables = pathDrawables.map { it.toDrawableComponentModel() },
    textDrawables = textDrawables.map { it.toDrawableComponentModel() }
)