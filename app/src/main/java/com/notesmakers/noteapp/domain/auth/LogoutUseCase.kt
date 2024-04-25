package com.notesmakers.noteapp.domain.auth

import com.notesmakers.auth.domain.AuthDomain
import com.notesmakers.auth.domain.TokenProvider
import org.koin.core.annotation.Factory

@Factory
class LogoutUseCase(private val authDomain: AuthDomain, private val tokenProvider: TokenProvider) {
    suspend operator fun invoke(): Boolean =
        tokenProvider.getTokens().refreshToken?.let { authDomain.logout(it) } ?: true

}