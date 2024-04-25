package com.notesmakers.noteapp.domain.auth

import com.notesmakers.auth.domain.AuthDomain
import com.notesmakers.auth.domain.TokenProvider
import org.koin.core.annotation.Factory

@Factory
class RegisterUserUseCase(private val authDomain: AuthDomain) {
    suspend operator fun invoke(
        name: String,
        surname: String,
        email: String,
        nickname: String,
        password: String
    ): Boolean =
        authDomain.registerUser(
            name = name,
            surname = surname,
            email = email,
            nickname = nickname,
            password = password
        )
}