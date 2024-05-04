package com.notesmakers.auth.domain

import com.notesmakers.auth.data.models.UserDetails


interface UserProvider {
    fun getUser(): UserDetails
    suspend fun saveUser(
        userName: String?,
        userEmail: String?,
        name: String?,
        surname: String?,
    )
}