package com.notesmakers.auth.domain

interface AuthErrorMapper {
    fun map(throwable: Throwable): String
}