package com.notesmakers.noteapp.data.notes.local

import java.util.UUID

data class TextNote(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
)

