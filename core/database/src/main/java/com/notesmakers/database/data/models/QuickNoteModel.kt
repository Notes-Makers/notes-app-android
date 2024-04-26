package com.notesmakers.database.data.models

import java.util.UUID

data class QuickNoteModel (
    val id: String = UUID.randomUUID().toString(),
    val text: String,
)