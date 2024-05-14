package com.notesmakers.auth.domain

import org.koin.core.annotation.Factory

@Factory
class RefreshTokenUseCase(
    private val authDomain: AuthDomain
) {
    suspend operator fun invoke(
        refreshToken: String
    ) = authDomain.refreshToken(refreshToken)
}