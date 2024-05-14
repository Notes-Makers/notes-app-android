package com.notesmakers.auth.data

import com.notesmakers.auth.LoginMutation
import com.notesmakers.auth.RefreshTokenMutation
import com.notesmakers.auth.data.models.SessionToken
import com.notesmakers.auth.data.models.UserDetails

fun RefreshTokenMutation.RefreshToken.toSessionToken() =
    SessionToken(token = token, refreshToken = refreshToken)

fun LoginMutation.Login.toSessionToken() =
    SessionToken(token = token, refreshToken = refreshToken)

fun LoginMutation.Login.toUserDetails() =
    UserDetails(
        userName = userName,
        userEmail = userEmail,
        name = name,
        surname = surname
    )