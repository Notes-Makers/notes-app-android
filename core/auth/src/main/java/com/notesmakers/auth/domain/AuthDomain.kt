package com.notesmakers.auth.domain

interface AuthDomain {
    suspend fun login(email: String, password: String): Boolean
    suspend fun logout(refreshToken: String): Boolean
    suspend fun refreshToken(refreshToken: String): Boolean
    suspend fun registerUser(
        name: String,
        surname: String,
        email: String,
        nickname: String,
        password: String
    ): Boolean
}