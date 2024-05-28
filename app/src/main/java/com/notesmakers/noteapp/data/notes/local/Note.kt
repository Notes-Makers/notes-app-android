package com.notesmakers.noteapp.data.notes.local

import java.time.ZonedDateTime
import java.util.UUID


data class Note(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: String?,
    val name: String,
    val description: String,
    val noteType: String,
    val pages: List<PageOutput>,
    val createdAt: ZonedDateTime,
    val createdBy: String,
    val modifiedBy: String,
    val modifiedAt: ZonedDateTime,
    val isPrivate: Boolean,
    val isShared: Boolean,
    val isPinned: Boolean,
    val tag: List<String> = listOf(),
)