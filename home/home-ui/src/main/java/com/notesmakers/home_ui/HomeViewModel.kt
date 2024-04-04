package com.notesmakers.home_ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notesmakers.database.data.models.Note
import com.notesmakers.home_domain.usecases.GetNotesUseCase
import com.notesmakers.home_domain.usecases.InsertNoteUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel

@KoinViewModel
class HomeViewModel(
//    private val insertNoteUseCase: InsertNoteUseCase,
//    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private val _notesEventFlow: MutableStateFlow<List<Note>> = MutableStateFlow(
        listOf()
    )
    var notesEventFlow = _notesEventFlow.asStateFlow()

    init {
        fetchAllNotes()
    }

    fun addNote() {
        viewModelScope.launch {
//            insertNoteUseCase(
//                Note(
//                    title = "test",
//                    description = "Description"
//                )
//            )
        }
    }

    fun fetchAllNotes() {
//        viewModelScope.launch {
//            _notesEventFlow.value = getNotesUseCase().stateIn(viewModelScope).value.list
//        }
    }
}
