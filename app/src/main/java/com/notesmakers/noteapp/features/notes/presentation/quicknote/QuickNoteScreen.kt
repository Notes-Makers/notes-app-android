package com.notesmakers.noteapp.features.notes.presentation.quicknote

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.notesmakers.noteapp.features.destinations.QuickNoteScreenDestination
import com.notesmakers.ui.composables.topappbar.TopBarCreation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun QuickNoteScreen(navigator: DestinationsNavigator) {
    QuickNoteScreen(onBackNav = {
        navigator.popBackStack()
    })
}

fun DestinationsNavigator.navToQuickNoteScreen() =
    navigate(QuickNoteScreenDestination())

@Composable
private fun QuickNoteScreen(onBackNav: () -> Unit) {
    Scaffold(
        topBar = {
            TopBarCreation(onBackNav = onBackNav)
        },
    ) { innerPadding ->
        QuickNote(modifier = Modifier.padding(innerPadding))
    }
}