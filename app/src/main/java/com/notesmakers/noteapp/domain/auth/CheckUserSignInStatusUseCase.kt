package com.notesmakers.noteapp.domain.auth

import com.notesmakers.auth.domain.TokenProvider
import org.koin.core.annotation.Factory

@Factory
class CheckUserSignInStatusUseCase(private val tokenProvider: TokenProvider) {
    operator fun invoke(): Boolean =
        tokenProvider.getTokens()
            .let { return@let (!it.refreshToken.isNullOrBlank() && !it.token.isNullOrBlank()) }
}