package com.notesmakers.network.data.api

data class ApiGetNotesInfo(
    val noteId: String?,
    val name: String?,
    val type: ApiNoteType?,
    val description: String?,
    val createdAt: Any?,
    val createdBy: String?,
    val modifiedAt: Any?,
    val modifiedBy: String?,
    val isPrivate: Boolean?,
    val isShared: Boolean?,
    val isDeleted: Boolean?,
    val pageSize: Int?,
    val itemSize: Int?,
)