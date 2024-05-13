package com.notesmakers.noteapp.presentation.notes.quicknote

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notesmakers.noteapp.presentation.destinations.QuickNoteScreenDestination
import com.notesmakers.noteapp.data.notes.local.Note
import com.notesmakers.noteapp.presentation.base.BaseViewModel
import com.notesmakers.noteapp.presentation.base.SnackbarHandler
import com.notesmakers.ui.composables.topappbar.TopBarCreation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Destination
@Composable
fun QuickNoteScreen(
    navigator: DestinationsNavigator,
    snackbarHandler: SnackbarHandler,
    noteId: String,
    quickNoteViewModel: QuickNoteViewModel = koinViewModel { parametersOf(noteId) },
) {
    val noteState = quickNoteViewModel.noteState.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        quickNoteViewModel.messageEvent.collect {
            when (it) {
                is BaseViewModel.MessageEvent.Error -> snackbarHandler.showErrorSnackbar(message = it.error)
                BaseViewModel.MessageEvent.Success -> Unit
            }

        }
    }
    QuickNoteScreen(
        onBackNav = {
            navigator.popBackStack()
        },
        note = noteState,
        updateTextNote = { text ->
            quickNoteViewModel.updateTextNote(noteId = noteId, text = text)
        },
        rewordTextNote = { text ->
            quickNoteViewModel.rewordText(text = text)
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
    rewordTextNote: (String) -> Unit,
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
            rewordTextNote = rewordTextNote
        )
    }
}