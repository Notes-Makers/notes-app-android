package com.notesmakers.noteapp.data.notes.local

import java.time.LocalDateTime
import java.util.UUID


data class Note(
    val id: String = UUID.randomUUID().toString(),
    val remoteId: String?,
    val name: String,
    val description: String,
    val noteType: String,
    val pages: List<PageOutput>,
    val createdAt: LocalDateTime,
    val createdBy: String,
    val modifiedBy: String,
    val modifiedAt: LocalDateTime,
    val isPrivate: Boolean,
    val isShared: Boolean,
    val isPinned: Boolean,
    val tag: List<String> = listOf(),
    val textNote: TextNote?,
)