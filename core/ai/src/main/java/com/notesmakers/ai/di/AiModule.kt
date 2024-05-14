package com.notesmakers.ai.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import com.notesmakers.ai.data.AiDomainImpl
import com.notesmakers.ai.data.ApolloAiClient
import com.notesmakers.ai.domain.AiDomain
import com.notesmakers.network.data.AuthorizationInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.notesmakers.ai")
class AiModule {
    @Single
    @Named("Ai")
    fun provideApolloClient(authorizationInterceptor: AuthorizationInterceptor): ApolloClient {
        return ApolloClient.Builder()
            .addHttpInterceptor(authorizationInterceptor)
            .httpEngine(DefaultHttpEngine(timeoutMillis = 4000))
            .serverUrl("http://10.0.2.2:8080/ai/graphql")
            .build()
    }
    @Single
    fun provideApolloAiClient(@Named("Ai") apolloClient: ApolloClient): ApolloAiClient {
        return ApolloAiClient(apolloClient)
    }
    @Single
    fun provideAiDomain(
        apolloAiClient: ApolloAiClient,
    ): AiDomain {
        return AiDomainImpl(apolloAiClient)
    }

}