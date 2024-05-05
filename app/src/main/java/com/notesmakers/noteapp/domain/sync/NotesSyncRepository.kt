package com.notesmakers.noteapp.domain.sync


interface NotesSyncRepository {
    suspend fun syncNotes()
}