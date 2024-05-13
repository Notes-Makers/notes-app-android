package com.notesmakers.ai.domain

interface AiDomain {
    suspend fun rewordText(text: String): String?
}