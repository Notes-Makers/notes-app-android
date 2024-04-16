package com.notesmakers.database.data.models

import com.notesmakers.database.data.entities.RealmNote
import io.realm.kotlin.ext.toRealmList
import java.util.UUID

data class DomainNoteModel(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val description: String,
    var bitmapDrawable: List<BitmapDrawableModel> = listOf(),
    var pathDrawables: List<PathDrawableModel> = listOf(),
    var textDrawables: List<TextDrawableModel> = listOf(),
)

fun RealmNote.toNoteData() = DomainNoteModel(
    id = id,
    title = title,
    description = description,
    bitmapDrawable = bitmapDrawable.map { it.toDrawableComponentModel() },
    pathDrawables = pathDrawables.map { it.toDrawableComponentModel() },
    textDrawables = textDrawables.map { it.toDrawableComponentModel() }
)

fun DomainNoteModel.toNote() = RealmNote(
    id = id,
    title = title,
    description = description,
    bitmapDrawable = bitmapDrawable.map { it.toRealmDrawableComponent() }.toRealmList(),
    pathDrawables = pathDrawables.map { it.toRealmDrawableComponent() }.toRealmList(),
    textDrawables = textDrawables.map { it.toRealmDrawableComponent() }.toRealmList(),
)