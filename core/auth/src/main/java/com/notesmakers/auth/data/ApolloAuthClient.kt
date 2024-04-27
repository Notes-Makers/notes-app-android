package com.notesmakers.auth.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Error
import com.notesmakers.auth.LoginMutation
import com.notesmakers.auth.LogoutMutation
import com.notesmakers.auth.RefreshTokenMutation
import com.notesmakers.auth.RegisterUserMutation
import com.notesmakers.auth.data.models.SessionToken
import com.notesmakers.auth.domain.AuthClient

class ApolloAuthClient(
    private val apolloClient: ApolloClient,
) : AuthClient {
    override suspend fun login(email: String, password: String): SessionToken? {
        val executed = apolloClient.mutation(LoginMutation(email, password))
            .execute()
        executed.errors?.takeIf { it.isNotEmpty() }?.first()?.let { error ->
            throwErrorIfExist(error)
        }
        return executed.data?.login?.toSessionToken()
    }

    override suspend fun logout(refreshToken: String) =
        apolloClient.mutation(LogoutMutation(refreshToken)).execute().data?.logout ?: false

    override suspend fun refreshToken(refreshToken: String) =
        apolloClient.mutation(RefreshTokenMutation(refreshToken))
            .execute().data?.refreshToken?.toSessionToken()


    override suspend fun registerUser(
        name: String, surname: String, email: String, nickname: String, password: String
    ): Boolean {
        val executed =
            apolloClient.mutation(RegisterUserMutation(name, surname, email, nickname, password))
                .execute()
        executed.errors?.takeIf { it.isNotEmpty() }?.first()?.let { error ->
            throwErrorIfExist(error)
        }
        return executed.data?.registerUser ?: false
    }

    private fun throwErrorIfExist(error: Error) {
        (error.extensions?.get("classification") as? Map<*, *>)?.let { classificationMap ->
            if (classificationMap["type"] as? String == "ExtendedValidationError")
                throw ExtendedValidationError(
                    error.message,
                    Throwable(classificationMap["constraint"] as? String)
                )
        }

        throw Exception(error.message)
    }
}