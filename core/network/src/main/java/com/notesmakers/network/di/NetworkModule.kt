package com.notesmakers.network.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import com.notesmakers.network.data.ApolloNetworkClient
import com.notesmakers.network.data.AuthorizationInterceptor
import com.notesmakers.network.data.NetworkDomainImpl
import com.notesmakers.network.domain.NetworkDomain
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.notesmakers.network")
class NetworkModule {
    @Single
    @Named("network")
    fun provideApolloClient(authorizationInterceptor: AuthorizationInterceptor): ApolloClient {
        return ApolloClient.Builder()
            .addHttpInterceptor(authorizationInterceptor)
            .httpEngine(DefaultHttpEngine(timeoutMillis = 4000))
            .serverUrl("http://10.0.2.2:8080/note/graphql")
            .build()
    }

    @Single
    fun provideApolloAuthClient(@Named("network") apolloClient: ApolloClient): ApolloNetworkClient {
        return ApolloNetworkClient(apolloClient)
    }
}