package com.notesmakers.noteapp.domain.notes

import com.notesmakers.file.domain.FileDomain
import org.koin.core.annotation.Factory

@Factory
class SetBitmapUseCase(private val fileDomain: FileDomain) {
    suspend operator fun invoke(noteId: String, itemId: String, base64String: String) =
        fileDomain.saveFile(
            noteId = noteId,
            itemId = itemId,
            base64String = base64String,
        )
}
