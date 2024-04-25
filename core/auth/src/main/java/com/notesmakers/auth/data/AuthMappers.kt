package com.notesmakers.auth.data

import com.notesmakers.auth.LoginMutation
import com.notesmakers.auth.RefreshTokenMutation
import com.notesmakers.auth.data.models.SessionToken

fun RefreshTokenMutation.RefreshToken.toSessionToken() =
    SessionToken(token = token, refreshToken = refreshToken)

fun LoginMutation.Login.toSessionToken() =
    SessionToken(token = token, refreshToken = refreshToken)
