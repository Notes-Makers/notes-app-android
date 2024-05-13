package com.notesmakers.ai.data

import com.notesmakers.ai.domain.AiDomain

class AiDomainImpl(
    private val apolloAiClient: ApolloAiClient,
) : AiDomain {
    override suspend fun rewordText(text: String): String? =
        runCatching { apolloAiClient.rewordText(text = text) }.getOrElse { throw  Exception() }.data?.rewordText
}