package com.notesmakers.noteapp.domain.auth

import com.notesmakers.auth.domain.AuthDomain
import com.notesmakers.auth.domain.TokenProvider
import org.koin.core.annotation.Factory

@Factory
class LoginUseCase(private val authDomain: AuthDomain) {
    suspend operator fun invoke(email: String, password: String): Boolean =
        authDomain.login(email, password)
}