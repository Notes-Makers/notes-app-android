package com.notesmakers.database.data.models

import java.util.UUID

data class DomainNoteModel(
    val id: String = UUID.randomUUID().toString(),
    var remoteNoteId: String?,
    val name: String,
    val description: String,
    val noteType: String,
    val pages: List<PageOutputModel>,
    val createdAt: Long,
    val createdBy: String,
    val modifiedBy: String,
    val modifiedAt: Long,
    val isPrivate: Boolean,
    val isShared: Boolean,
    val isPinned: Boolean,
    val tag: List<String> = listOf(),
)

