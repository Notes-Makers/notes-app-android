package com.notesmakers.auth.data

import com.notesmakers.auth.data.models.SessionToken
import com.notesmakers.auth.domain.AuthDomain
import com.notesmakers.auth.domain.TokenProvider
import com.notesmakers.auth.domain.UserProvider

class AuthDomainImpl(
    private val apolloAuthClient: ApolloAuthClient,
    private val tokenProvider: TokenProvider,
    private val userProvider: UserProvider,
) : AuthDomain {
    override suspend fun login(email: String, password: String): Boolean =
        runCatching {
            apolloAuthClient.login(
                email = email,
                password = password
            )
        }.onSuccess {
            val sessionToken = it.first
            val userDetails = it.second
            userProvider.saveUser(
                userName = userDetails?.userName,
                userEmail = userDetails?.userEmail,
                name = userDetails?.name,
                surname = userDetails?.surname
            )
            tokenProvider.saveTokens(sessionToken?.token!!, sessionToken.refreshToken!!)
        }.map {
            it.first != null
        }.getOrElse { exception ->
            throw LoginException(cause = exception, message = exception.message)
        }

    override suspend fun logout(refreshToken: String): Boolean =
        runCatching { apolloAuthClient.logout(refreshToken = refreshToken) }.onSuccess {
            tokenProvider.saveTokens(null, null)
            userProvider.saveUser(null, null, null, null)
        }.getOrElse { exception ->
            throw LogoutException(cause = exception, message = exception.message)
        }


    override suspend fun refreshToken(refreshToken: String): SessionToken =
        runCatching {
            apolloAuthClient.refreshToken(refreshToken = refreshToken)!!
        }.onSuccess {
            tokenProvider.saveTokens(it.token!!, it.refreshToken!!)
        }.getOrElse { exception ->
            tokenProvider.saveTokens(null, null)
            throw RefreshTokenException(cause = exception, message = exception.message)
        }

    override suspend fun registerUser(
        name: String,
        surname: String,
        email: String,
        nickname: String,
        password: String
    ): Boolean =
        runCatching {
            apolloAuthClient.registerUser(
                name = name,
                surname = surname,
                email = email,
                nickname = nickname,
                password = password
            )
        }.getOrElse { exception ->
            throw RegisterUserException(cause = exception, message = exception.message)
        }
}