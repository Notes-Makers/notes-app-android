package com.notesmakers.file.data

import android.util.Base64
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.DefaultUpload
import com.notesmakers.file.GetFileQuery
import com.notesmakers.file.SaveFileMutation
import com.notesmakers.file.domain.FileClient
import java.io.IOException
import java.util.UUID

class ApolloFileClient(
    private val apolloClient: ApolloClient,
) : FileClient {
    override suspend fun saveFile(noteId: String, itemId: String, base64String: String) {

        val fileRequestBody = try {
            base64ToFileRequestBody(base64String)
        } catch (e: IOException) {
            println("Failed to create RequestBody from Base64 string: ${e.message}")
            return
        }

        val upload = DefaultUpload.Builder()
            .fileName("${UUID.randomUUID()}.png")
            .content(fileRequestBody)
            .build()
        apolloClient.mutation(
            SaveFileMutation(
                noteId = noteId,
                itemId = itemId,
                file = upload
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

    private fun base64ToFileRequestBody(base64String: String): ByteArray {
        val decodedBytes: ByteArray = try {
            Base64.decode(base64String, Base64.DEFAULT)
        } catch (e: IllegalArgumentException) {
            throw IOException("Base64 decoding failed", e)
        }

        return decodedBytes
    }

}