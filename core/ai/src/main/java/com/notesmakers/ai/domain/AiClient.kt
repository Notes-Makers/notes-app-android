package com.notesmakers.ai.domain

import com.apollographql.apollo3.ApolloCall
import com.apollographql.apollo3.api.ApolloResponse
import com.notesmakers.ai.RewordTextMutation

interface AiClient {
   suspend fun rewordText(text: String): ApolloResponse<RewordTextMutation.Data>
}