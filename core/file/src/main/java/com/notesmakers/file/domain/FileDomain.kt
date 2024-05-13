package com.notesmakers.file.domain

interface FileDomain {
    suspend fun saveFile(noteId: String, itemId: String, base64String: String)
    suspend fun getFile(noteId: String, itemId: String): String
}