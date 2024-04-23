package com.notesmakers.auth.data

import com.notesmakers.auth.domain.AuthDomain
import com.notesmakers.auth.domain.TokenProvider

class AuthDomainImpl(
    private val apolloAuthClient: ApolloAuthClient,
    private val tokenProvider: TokenProvider,
) : AuthDomain {
    override suspend fun login(email: String, password: String): Boolean =
        runCatching {
            apolloAuthClient.login(
                email = email,
                password = password
            )
        }.onSuccess {
            tokenProvider.saveTokens(it?.token!!, it.refreshToken!!)
        }.map {
            it != null
        }.getOrElse { exception ->
            throw LoginException(cause = exception, message = exception.message)
        }

    override suspend fun logout(refreshToken: String): Boolean =
        runCatching { apolloAuthClient.logout(refreshToken = refreshToken) }.onSuccess {

        }.getOrElse { exception ->
            throw LogoutException(cause = exception, message = exception.message)
        }


    override suspend fun refreshToken(refreshToken: String): Boolean =
        runCatching { apolloAuthClient.refreshToken(refreshToken = refreshToken) }
            .onSuccess {
                tokenProvider.saveTokens(it?.token!!, it.refreshToken!!)
            }.map {
                it != null
            }.getOrElse { exception ->
                throw LoginException(cause = exception, message = exception.message)
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