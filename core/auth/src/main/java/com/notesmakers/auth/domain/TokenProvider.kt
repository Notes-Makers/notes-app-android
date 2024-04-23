package com.notesmakers.auth.domain

import com.notesmakers.auth.data.models.SessionToken


interface TokenProvider {
    fun getTokens(): SessionToken
    suspend fun saveTokens(jtwToken: String, refreshToken: String)
}