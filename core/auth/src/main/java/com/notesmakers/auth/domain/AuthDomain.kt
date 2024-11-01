package com.notesmakers.auth.domain

import com.notesmakers.auth.data.models.SessionToken

interface AuthDomain {
    suspend fun login(email: String, password: String): Boolean
    suspend fun logout(refreshToken: String): Boolean
    suspend fun refreshToken(refreshToken: String): SessionToken
    suspend fun registerUser(
        name: String,
        surname: String,
        email: String,
        nickname: String,
        password: String
    ): Boolean
}