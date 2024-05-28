package com.notesmakers.file.data

import com.apollographql.apollo3.ApolloClient
import com.notesmakers.file.GetFileQuery
import com.notesmakers.file.SaveFileBase64Mutation
import com.notesmakers.file.domain.FileClient

class ApolloFileClient(
    private val apolloClient: ApolloClient,
) : FileClient {
    override suspend fun saveFile(noteId: String, itemId: String, base64String: String) {

        apolloClient.mutation(
            SaveFileBase64Mutation(
                noteId = noteId,
                itemId = itemId,
                file = base64String
            )
        ).execute()
    }

    override suspend fun getFile(noteId: String, itemId: String): String =
        apolloClient.query(
            GetFileQuery(
                noteId = noteId,
                itemId = itemId,
            )
        ).execute().data?.getFile ?: ""

}