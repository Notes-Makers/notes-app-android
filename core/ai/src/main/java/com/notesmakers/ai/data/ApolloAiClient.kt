package com.notesmakers.ai.data

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.notesmakers.ai.RewordTextMutation
import com.notesmakers.ai.domain.AiClient

class ApolloAiClient(private val apolloClient: ApolloClient) : AiClient {
    override suspend fun rewordText(text: String): ApolloResponse<RewordTextMutation.Data> =
        apolloClient.mutation(RewordTextMutation(text)).execute()
}