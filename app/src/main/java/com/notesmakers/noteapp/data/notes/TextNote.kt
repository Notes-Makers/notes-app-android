package com.notesmakers.noteapp.data.notes

import java.util.UUID

data class TextNote(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
)

