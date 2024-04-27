package com.notesmakers.noteapp.domain.auth

import com.notesmakers.auth.domain.AuthErrorMapper
import org.koin.core.annotation.Factory

@Factory
class GetAuthErrorMapperUseCase(private val authErrorMapper: AuthErrorMapper) {
    operator fun invoke(throwable: Throwable): String =
        authErrorMapper.map(throwable)
}