package com.notesmakers.auth.di

import android.content.Context
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import com.notesmakers.auth.data.ApolloAuthClient
import com.notesmakers.auth.data.AuthDomainImpl
import com.notesmakers.auth.data.TokenProviderImpl
import com.notesmakers.auth.domain.AuthDomain
import com.notesmakers.auth.domain.TokenProvider
import com.notesmakers.auth.domain.UserProvider
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.notesmakers.auth")
class AuthModule {
    @Single
    @Named("Auth")
    fun provideApolloClient(): ApolloClient {
        return ApolloClient.Builder()
            .httpEngine(DefaultHttpEngine(timeoutMillis = 4000))
            .serverUrl("http://10.0.2.2:8080/auth/graphql")
            .build()
    }

    @Single
    fun provideApolloAuthClient(@Named("Auth") apolloClient: ApolloClient): ApolloAuthClient {
        return ApolloAuthClient(apolloClient)
    }

    @Single
    fun provideAuthDomain(
        apolloAuthClient: ApolloAuthClient,
        tokenProvider: TokenProvider,
        userProvider: UserProvider,
    ): AuthDomain {
        return AuthDomainImpl(apolloAuthClient, tokenProvider, userProvider)
    }

    @Single
    fun provideTokenProvider(
        context: Context
    ): TokenProvider {
        return TokenProviderImpl(context)
    }
}