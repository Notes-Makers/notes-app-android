package com.notesmakers.file.domain


interface FileClient {
    suspend fun saveFile(noteId: String, itemId: String, base64String: String)
    suspend fun getFile(noteId: String, itemId: String): String
}