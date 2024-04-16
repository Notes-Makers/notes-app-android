package com.notesmakers.noteapp.features.notes.domain

import com.notesmakers.noteapp.di.DatabaseDomainModule
import com.notesmakers.noteapp.features.notes.data.Note
import com.notesmakers.noteapp.features.notes.data.TextDrawable
import org.koin.core.annotation.Factory

@Factory
class InsertNoteUseCase(
    private val databaseDomainModule: DatabaseDomainModule
) {
    suspend operator fun invoke(title: String, description: String) =
        databaseDomainModule.insertNote(
            note = Note(
                title = title,
                description = description,
                pathDrawables = listOf(),
                bitmapDrawables = listOf(),
                textDrawables = listOf(
                    TextDrawable(
                        text = "Tamatha",
                        color = (0x000),
                        offsetX = 100f, offsetY = 100f, timestamp = System.currentTimeMillis(),
                    )
                ),
            )
        )
}