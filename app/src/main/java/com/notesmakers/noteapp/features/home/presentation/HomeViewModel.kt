package com.notesmakers.noteapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.features.notes.domain.AddTextDrawableToNoteUseCase
import com.notesmakers.noteapp.features.notes.domain.GetNotesUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    getNotesUseCase: GetNotesUseCase,
    private val addTextDrawableToNoteUseCase: AddTextDrawableToNoteUseCase,
) : ViewModel() {
    var notesEventFlow = getNotesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        emptyList()
    )

    fun test(noteId: String) = viewModelScope.launch {
        addTextDrawableToNoteUseCase(
            noteId = noteId,
            text = "Vanity",
            color = 3834L,
            notePageIndex = 0,
            offsetX = 0f,
            offsetY = 0f
        )
    }
}
