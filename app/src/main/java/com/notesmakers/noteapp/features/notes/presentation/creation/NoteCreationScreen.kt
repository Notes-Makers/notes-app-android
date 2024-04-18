package com.notesmakers.noteapp.features.notes.presentation.creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.notesmakers.noteapp.features.destinations.NoteCreationScreenDestination
import com.notesmakers.noteapp.features.notes.presentation.paintnote.navToPaintNote
import com.notesmakers.noteapp.features.notes.presentation.quicknote.navToQuickNoteScreen
import com.notesmakers.ui.composables.buttons.BaseButton
import com.notesmakers.ui.composables.inputs.BaseTextField
import com.notesmakers.ui.composables.topappbar.TopBarCreation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@Composable
@Destination
fun NoteCreationScreen(
    noteCreationViewModel: NoteCreationViewModel = koinViewModel(),
    navigator: DestinationsNavigator,
    noteMode: NoteMode = NoteMode.QUICK_NOTE
) {
    LaunchedEffect(Unit) {
        noteCreationViewModel.noteCreationEvent.collect {
            when (it) {
                is NoteCreationViewModel.NoteCreationEvent.NavToNote -> {
                    navigator.popBackStack()
                    when (noteMode) {
                        NoteMode.QUICK_NOTE -> navigator.navToQuickNoteScreen()
                        NoteMode.PAINT_NOTE -> {
                            it.note.id?.let { noteId ->
                                navigator.navToPaintNote(noteId)
                            }
                        }
                    }
                }
            }
        }
    }
    NoteCreationScreen(
        onBackNav = { navigator.popBackStack() },
        noteMode = noteMode,
        onCreateNote = { title, description ->
            noteCreationViewModel.createNote(
                title = title,
                description = description,
                ownerId = "",
                noteType = noteMode.toNoteType()
            )
        },
    )
}

fun DestinationsNavigator.navToNoteCreation(noteMode: NoteMode) =
    navigate(NoteCreationScreenDestination(noteMode = noteMode))

@Composable
private fun NoteCreationScreen(
    onBackNav: () -> Unit,
    noteMode: NoteMode,
    onCreateNote: (String, String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarCreation(onBackNav = onBackNav)
        },
    ) { innerPadding ->
        CreationPage(
            modifier = Modifier.padding(innerPadding),
            noteMode = noteMode,
            onCreateNote = onCreateNote,
        )
    }
}

@Composable
private fun CreationPage(
    modifier: Modifier,
    noteMode: NoteMode,
    onCreateNote: (title: String, desc: String) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = noteMode.icon),
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .size(40.dp)
            )
            Column {
                Text(text = noteMode.title, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.padding(bottom = 4.dp))
                Text(
                    text = noteMode.desc,
                    fontWeight = FontWeight.Normal
                )
                Spacer(modifier = Modifier.padding(bottom = 4.dp))
                HorizontalDivider()
            }

        }
        BaseTextField(
            modifier = Modifier.padding(bottom = 12.dp),
            onValueChange = { title = it },
            labelText = "Enter title",
            placeholderText = "title",
            focusManager = focusManager,
            errorMessage = null,
        )
        BaseTextField(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .defaultMinSize(minHeight = 300.dp)
                .fillMaxWidth(),
            onValueChange = { description = it },
            labelText = "Enter description",
            placeholderText = "description",
            focusManager = focusManager,
            errorMessage = null,
            maxLines = 5,
            singleLine = false
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()) {
            BaseButton(
                modifier = Modifier,
                label = "Create",
                onClick = {
                    onCreateNote(title, description)
                },
            )
        }
    }
}