package com.notesmakers.noteapp.data.notes.api

data class BasePage(
    val id: String?,
    val isDeleted: Boolean?,
    val createdAt: Any?,
    val createdBy: String?,
    val modifiedAt: Any?,
    val modifiedBy: String?,
    val items: List<BaseItem?>?,
)