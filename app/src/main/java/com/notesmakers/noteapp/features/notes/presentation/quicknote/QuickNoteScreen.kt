package com.notesmakers.noteapp.features.notes.presentation.quicknote

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notesmakers.noteapp.features.destinations.QuickNoteScreenDestination
import com.notesmakers.noteapp.features.notes.data.Note
import com.notesmakers.ui.composables.topappbar.TopBarCreation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination
@Composable
fun QuickNoteScreen(
    navigator: DestinationsNavigator,
    noteId: String,
    quickNoteViewModel: QuickNoteViewModel = koinViewModel { parametersOf(noteId) },
) {
    val noteState = quickNoteViewModel.noteState.collectAsStateWithLifecycle().value

    QuickNoteScreen(
        onBackNav = {
            navigator.popBackStack()
        },
        note = noteState,
        updateTextNote = { text ->
            quickNoteViewModel.updateTextNote(noteId = noteId, text = text)
        }
    )
}

fun DestinationsNavigator.navToQuickNoteScreen(noteId: String) =
    navigate(QuickNoteScreenDestination(noteId = noteId))

@Composable
private fun QuickNoteScreen(
    onBackNav: () -> Unit,
    note: Note?,
    updateTextNote: (String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarCreation(onBackNav = onBackNav)
        },
    ) { innerPadding ->
        QuickNote(
            modifier = Modifier.padding(innerPadding),
            text = note?.textNote?.text ?: "",
            updateTextNote = updateTextNote,
        )
    }
}