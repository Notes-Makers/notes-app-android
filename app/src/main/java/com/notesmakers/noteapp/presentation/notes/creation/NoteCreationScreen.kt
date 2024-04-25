package com.notesmakers.noteapp.presentation.notes.creation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notesmakers.noteapp.presentation.destinations.NoteCreationScreenDestination
import com.notesmakers.noteapp.presentation.notes.paintnote.navToPaintNote
import com.notesmakers.noteapp.presentation.notes.quicknote.navToQuickNoteScreen
import com.notesmakers.ui.composables.inputs.BaseTextField
import com.notesmakers.ui.composables.topappbar.TopBarCreation
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
@Destination
fun NoteCreationScreen(
    noteId: String?,
    noteCreationViewModel: NoteCreationViewModel = koinViewModel { parametersOf(noteId) },
    navigator: DestinationsNavigator,
    noteMode: NoteMode = NoteMode.QUICK_NOTE,
    resultNavigator: ResultBackNavigator<Boolean>,
) {
    val noteCreationState =
        noteCreationViewModel.noteCreationState.collectAsStateWithLifecycle().value
    LaunchedEffect(Unit) {
        noteCreationViewModel.noteCreationEvent.collect {
            when (it) {
                is NoteCreationViewModel.NoteCreationEvent.NavToNote -> {
                    navigator.popBackStack()
                    when (noteMode) {
                        NoteMode.QUICK_NOTE -> it.note.id?.let { noteId ->
                            navigator.navToQuickNoteScreen(
                                noteId
                            )
                        }

                        NoteMode.PAINT_NOTE -> {
                            it.note.id?.let { noteId ->
                                navigator.navToPaintNote(noteId)
                            }
                        }
                    }
                }

                NoteCreationViewModel.NoteCreationEvent.BackEndUpdate -> resultNavigator.navigateBack(
                    true
                )
            }
        }
    }
    NoteCreationScreen(
        isEditMode = noteId != null,
        noteCreationState = noteCreationState as? NoteCreationViewModel.NoteCreationState.Success,
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
        onUpdateNote = { title, description ->
            noteId?.let { id ->
                noteCreationViewModel.updateNote(
                    noteId = id,
                    title = title,
                    description = description,
                )
            }
        }
    )
}

fun DestinationsNavigator.navToNoteCreation(noteMode: NoteMode, noteId: String? = null) =
    navigate(NoteCreationScreenDestination(noteMode = noteMode, noteId = noteId))

@Composable
private fun NoteCreationScreen(
    isEditMode: Boolean,
    noteCreationState: NoteCreationViewModel.NoteCreationState.Success?,
    onBackNav: () -> Unit,
    noteMode: NoteMode,
    onCreateNote: (String, String) -> Unit,
    onUpdateNote: (title: String, desc: String) -> Unit,
) {
    Scaffold(
        topBar = {
            TopBarCreation(onBackNav = onBackNav)
        },
    ) { innerPadding ->
        CreationPage(
            modifier = Modifier.padding(innerPadding),
            isEditMode = isEditMode,
            noteMode = noteMode,
            onCreateNote = onCreateNote,
            onUpdateNote = onUpdateNote,
            initTitle = noteCreationState?.note?.title ?: "",
            initDesc = noteCreationState?.note?.description ?: ""
        )
    }
}

@Composable
private fun CreationPage(
    modifier: Modifier,
    initTitle: String,
    initDesc: String,
    noteMode: NoteMode,
    isEditMode: Boolean,
    onCreateNote: (title: String, desc: String) -> Unit,
    onUpdateNote: (title: String, desc: String) -> Unit,
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
            initValue = initTitle,
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
            initValue = initDesc,
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
            Button(
                onClick = {
                    if (isEditMode) {
                        onUpdateNote(title, description)
                    } else {
                        onCreateNote(title, description)
                    }
                },
                modifier = Modifier.padding(end = 8.dp)
            ) {
                Text(text = if (isEditMode) "Apply" else "Create")
            }
        }
    }
}