package com.notesmakers.noteapp.domain.auth

import com.notesmakers.auth.domain.UserProvider
import org.koin.core.annotation.Factory

@Factory
class GetOwnerUseCase(private val userProvider: UserProvider) {
    operator fun invoke(): String = userProvider.getUser().userEmail ?: ""
}

