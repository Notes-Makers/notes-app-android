package com.notesmakers.file.data

import com.notesmakers.file.domain.FileDomain

class FileDomainImpl(
    private val apolloFileClient: ApolloFileClient
) : FileDomain {
    override suspend fun saveFile(noteId: String, itemId: String, base64String: String) =
        runCatching {
            apolloFileClient.saveFile(noteId, itemId, base64String)
        }.getOrElse {
            throw Exception("Not Saved")
        }

    override suspend fun getFile(noteId: String, itemId: String): String =
        runCatching {
            apolloFileClient.getFile(noteId, itemId)
        }.getOrElse {
            throw Exception("Not File")
        }

}