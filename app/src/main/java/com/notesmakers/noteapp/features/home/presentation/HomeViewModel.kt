package com.notesmakers.noteapp.features.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notesmakers.noteapp.features.notes.domain.GetNotesUseCase
import com.notesmakers.noteapp.features.notes.domain.InsertNoteUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
    private val insertNoteUseCase: InsertNoteUseCase,
    private val getNotesUseCase: GetNotesUseCase,
) : ViewModel() {


    var notesEventFlow = getNotesUseCase().stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(),
        emptyList()
    )

    init {
        fetchAllNotes()
    }

    fun addNote(title: String, description: String) = viewModelScope.launch {
        insertNoteUseCase(
            title = title,
            description = description,
        )
    }
//    fun addNote() {
//        viewModelScope.launch {
//            insertNoteUseCase(
//                Note(
//                    title = "test",
//                    description = "Description"
//                )
//            )
//        }
//    }

    fun fetchAllNotes() {
//        viewModelScope.launch {
//            _notesEventFlow.value = getNotesUseCase().stateIn(viewModelScope).value.list
//        }
    }
}
