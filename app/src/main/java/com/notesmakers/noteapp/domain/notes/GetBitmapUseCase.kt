package com.notesmakers.noteapp.domain.notes

import com.notesmakers.file.domain.FileDomain
import org.koin.core.annotation.Factory

@Factory
class GetBitmapUseCase(private val fileDomain: FileDomain) {
    suspend operator fun invoke(noteId: String, itemId: String): String =
        fileDomain.getFile(
            noteId = noteId,
            itemId = itemId
        )
}
