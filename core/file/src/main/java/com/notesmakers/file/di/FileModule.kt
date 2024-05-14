package com.notesmakers.file.di

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.network.http.DefaultHttpEngine
import com.notesmakers.file.data.ApolloFileClient
import com.notesmakers.file.data.FileDomainImpl
import com.notesmakers.file.domain.FileDomain
import com.notesmakers.network.data.AuthorizationInterceptor
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Named
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.notesmakers.file")
class FileModule {
    @Single
    @Named("file")
    fun provideApolloClient(authorizationInterceptor: AuthorizationInterceptor): ApolloClient {
        return ApolloClient.Builder()
            .addHttpInterceptor(authorizationInterceptor)
            .httpEngine(DefaultHttpEngine(timeoutMillis = 4000))
            .serverUrl("http://10.0.2.2:8080/file/graphql")
            .build()
    }

    @Single
    fun provideApolloAuthClient(@Named("file") apolloClient: ApolloClient): ApolloFileClient {
        return ApolloFileClient(apolloClient)
    }

    @Single
    fun provideFileDomain(
        apolloFileClient: ApolloFileClient,
    ): FileDomain {
        return FileDomainImpl(apolloFileClient)
    }

}