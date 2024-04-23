package com.notesmakers.auth.data

import com.apollographql.apollo3.ApolloClient
import com.notesmakers.auth.LoginMutation
import com.notesmakers.auth.LogoutMutation
import com.notesmakers.auth.RefreshTokenMutation
import com.notesmakers.auth.RegisterUserMutation
import com.notesmakers.auth.domain.AuthClient

class ApolloAuthClient(
    private val apolloClient: ApolloClient
) : AuthClient {
    override suspend fun login(email: String, password: String) =
        apolloClient.mutation(LoginMutation(email, password))
            .execute()
            .data
            ?.login
            ?.toSessionToken()

    override suspend fun logout(refreshToken: String) =
        apolloClient.mutation(LogoutMutation(refreshToken))
            .execute()
            .data
            ?.logout ?: false

    override suspend fun refreshToken(refreshToken: String) =
        apolloClient.mutation(RefreshTokenMutation(refreshToken))
            .execute()
            .data
            ?.refreshToken?.toSessionToken()


    override suspend fun registerUser(
        name: String,
        surname: String,
        email: String,
        nickname: String,
        password: String
    ) = apolloClient
        .mutation(RegisterUserMutation(name, surname, email, nickname, password))
        .execute()
        .data
        ?.registerUser ?: false
}