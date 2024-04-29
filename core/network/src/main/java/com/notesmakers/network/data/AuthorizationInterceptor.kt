package com.notesmakers.network.data

import com.apollographql.apollo3.api.http.HttpRequest
import com.apollographql.apollo3.api.http.HttpResponse
import com.apollographql.apollo3.network.http.HttpInterceptor
import com.apollographql.apollo3.network.http.HttpInterceptorChain
import com.notesmakers.auth.domain.RefreshTokenUseCase
import com.notesmakers.auth.domain.TokenProvider
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.koin.core.annotation.Single

@Single
class AuthorizationInterceptor(
    private val tokenProvider: TokenProvider,
    private val refreshTokenUseCase: RefreshTokenUseCase,
) : HttpInterceptor {
    private val mutex = Mutex()

    override suspend fun intercept(
        request: HttpRequest,
        chain: HttpInterceptorChain
    ): HttpResponse {
        var token = mutex.withLock {
            tokenProvider.getTokens().token
        }

        val response =
            chain.proceed(request.newBuilder().addHeader("Authorization", "Bearer $token").build())

        return if (response.statusCode == 401) {
            token = mutex.withLock {
                refreshTokenUseCase(tokenProvider.getTokens().refreshToken!!).token
            }
            chain.proceed(request.newBuilder().addHeader("Authorization", "Bearer $token").build())
        } else {
            response
        }
    }
}
