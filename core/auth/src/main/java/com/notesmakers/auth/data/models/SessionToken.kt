package com.notesmakers.auth.data.models

data class SessionToken(
    val token: String?,
    val refreshToken: String?,
)