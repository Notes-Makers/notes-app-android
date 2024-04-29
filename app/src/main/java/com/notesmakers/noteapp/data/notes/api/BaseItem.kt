package com.notesmakers.noteapp.data.notes.api

import com.notesmakers.network.type.ItemType

data class BaseItem(
    val id: String?,
    val type: ItemType?,
    val isDeleted: Boolean?,
    val content: String?,
    val createdAt: Any?,
    val createdBy: String?,
    val modifiedAt: Any?,
    val modifiedBy: String?,
    val hash: String?,
)