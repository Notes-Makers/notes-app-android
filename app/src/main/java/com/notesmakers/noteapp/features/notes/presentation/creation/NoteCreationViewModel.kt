package com.notesmakers.noteapp.features.notes.presentation.creation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.features.notes.domain.CreateNoteUseCase
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class NoteCreationViewModel(
    private val createNoteUseCase: CreateNoteUseCase,
) : ViewModel() {
    fun createNote(
        title: String,
        description: String,
        ownerId: String,
        noteType: String
    ) = viewModelScope.launch {
        createNoteUseCase(
            title = title,
            description = description,
            ownerId = ownerId,
            noteType = noteType
        )
    }
}