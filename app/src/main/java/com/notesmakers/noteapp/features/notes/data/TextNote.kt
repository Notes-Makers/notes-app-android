package com.notesmakers.noteapp.features.notes.data

import java.util.UUID

data class TextNote(
    var id: String = UUID.randomUUID().toString(),
    var text: String,
)

