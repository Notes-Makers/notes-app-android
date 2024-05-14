package com.notesmakers.noteapp.data.notes.api

import com.notesmakers.noteapp.data.notes.local.NoteDrawableType

data class BaseNote(
    val id: String?,
    val name: String?,
    val type: NoteDrawableType?,
    val description: String?,
    val createdAt: Any?,
    val createdBy: String?,
    val modifiedAt: Any?,
    val modifiedBy: String?,
    val isPrivate: Boolean?,
    val isShared: Boolean?,
    val isDeleted: Boolean?,
    val tag: List<String?>?,
    val pages: List<BasePage>,
)
