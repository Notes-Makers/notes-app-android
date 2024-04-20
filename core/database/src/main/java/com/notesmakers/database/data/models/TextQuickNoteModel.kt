package com.notesmakers.database.data.models

import java.util.UUID

data class TextQuickNoteModel (
    val id: String = UUID.randomUUID().toString(),
    val text: String,
)