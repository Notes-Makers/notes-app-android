@file:OptIn(ExperimentalFoundationApi::class)

package com.notesmakers.noteapp.presentation.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notesmakers.noteapp.data.notes.Note
import com.notesmakers.noteapp.data.notes.NoteDrawableType
import com.notesmakers.noteapp.data.notes.toNoteDrawableType
import com.notesmakers.noteapp.extension.PATTERN
import com.notesmakers.noteapp.presentation.auth.login.goToLoginScreenDestination
import com.notesmakers.noteapp.presentation.destinations.LoginScreenDestination
import com.notesmakers.noteapp.presentation.home.components.BaseTopAppBar
import com.notesmakers.noteapp.presentation.notes.creation.navToNoteCreation
import com.notesmakers.noteapp.presentation.notes.paintnote.navToPaintNote
import com.notesmakers.noteapp.presentation.notes.quicknote.navToQuickNoteScreen
import com.notesmakers.ui.animations.getEnterScrollTransition
import com.notesmakers.ui.animations.getExitScrollTransition
import com.notesmakers.ui.composables.ChipItem
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.composables.inputs.SearchBar
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.format.DateTimeFormatter

@Destination
@Composable
fun HomeScreen(
    navigator: DestinationsNavigator,
    resultRecipient: ResultRecipient<LoginScreenDestination, Boolean>,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val userIsLoggedIn = viewModel.userIsLoggedIn.collectAsStateWithLifecycle().value
    val selectedNote = viewModel.selectedNote.collectAsStateWithLifecycle().value
    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Canceled -> {}

            is NavResult.Value -> {
                if (result.value) {
                    viewModel.checkUserSignIn()
                }
            }
        }
    }
    Scaffold(
        topBar = {
            BaseTopAppBar(
                userIsLoggedIn = userIsLoggedIn,
                accountIconAction = {
                    navigator.goToLoginScreenDestination()
                },
                navToNote = { navigator.navToNoteCreation(it) },
                logout = { viewModel.logout() }
            )
        },
    ) { innerPadding ->
        HomeScreen(
            innerPadding = innerPadding,
            notes = viewModel.notesEventFlow.collectAsStateWithLifecycle().value,
            navToNote = { noteID, noteType ->
                when (noteType.toNoteDrawableType()) {
                    NoteDrawableType.QUICK_NOTE -> navigator.navToQuickNoteScreen(noteID)
                    NoteDrawableType.PAINT_NOTE -> navigator.navToPaintNote(noteID)
                    NoteDrawableType.UNDEFINED -> Unit
                }
            },
            onNoteSelected = {
                viewModel.onSelectNote(note = it)
            }
        )
        when (selectedNote) {
            HomeViewModel.NoteSelectedStatus.None -> Unit
            is HomeViewModel.NoteSelectedStatus.Selected -> NoteInfoDialog(
                note = selectedNote.note,
                onEditNote = {

                },
                onDeleteNote = {
                    viewModel.onDeleteNote(note = selectedNote.note)
                },
                onDismiss = viewModel::onDismissNote

            )
        }
    }
}

@Composable
private fun HomeScreen(
    innerPadding: PaddingValues,
    notes: List<Note>,
    navToNote: (String, String) -> Unit,
    onNoteSelected: (Note) -> Unit
) {
    val listState = rememberLazyStaggeredGridState()
    val showButton by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 0
        }
    }
    Box {
        NoteGridLayout(
            listState = listState,
            innerPadding = innerPadding,
            notes = notes,
            navToNote = navToNote,
            onNoteSelected = onNoteSelected
        )
        ScrollToTopButton(
            modifier = Modifier.align(Alignment.BottomCenter),
            showButton = showButton,
            listState = listState,
        )
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun NoteGridLayout(
    listState: LazyStaggeredGridState,
    innerPadding: PaddingValues,
    notes: List<Note>,
    navToNote: (String, String) -> Unit,
    onNoteSelected: (Note) -> Unit
) {
    var isCategoryVisible by remember {
        mutableStateOf(false)
    }
    LazyVerticalStaggeredGrid(
        state = listState,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .padding(10.dp),
        columns = StaggeredGridCells.Fixed(2),
        verticalItemSpacing = 8.dp,
        horizontalArrangement = Arrangement.spacedBy(18.dp),
    ) {
        item(span = StaggeredGridItemSpan.FullLine) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                SearchBar(hint = "Search", modifier = Modifier.weight(1f))
                BaseIconButton(
                    onClick = { isCategoryVisible = !isCategoryVisible },
                    painterResource = com.notesmakers.common_ui.R.drawable.collections_bookmark,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            AnimatedVisibility(visible = isCategoryVisible) {
                FlowRow(modifier = Modifier.padding(8.dp)) {
                    ChipItem("Price: High to Low")
                    ChipItem("Avg rating: 4+")
                    ChipItem("Free breakfast")
                    ChipItem("Free cancellation")
                    ChipItem("£50 pn")
                }
            }
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(com.notesmakers.common_ui.R.drawable.keep),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
                Text(text = "Pinned", fontSize = 20.sp)
            }

        }
        items(2) { item ->
            ItemNote(
                title = getTitleContent() ?: "",
                textContent = getTextContent(),
                dateTime = "10 march 2021",
                onClick = {},
                onLongClick = {}
            )
        }
        item(span = StaggeredGridItemSpan.FullLine) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    painter = painterResource(com.notesmakers.common_ui.R.drawable.note_stack),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(30.dp)
                )
                Text(text = "My notes", fontSize = 20.sp)
            }
        }
        items(notes.size) { index ->
            ItemNote(
                title = notes[index].title,
                textContent = notes[index].description,
                dateTime = notes[index].createdAt.format(DateTimeFormatter.ofPattern(PATTERN)),
                onClick = {
                    notes[index].id?.let { navToNote(it, notes[index].noteType) }
                },
                onLongClick = {
                    onNoteSelected(notes[index])
                }
            )
        }
    }
}

@Composable
fun NoteInfoDialog(
    modifier: Modifier = Modifier,
    note: Note,
    onEditNote: () -> Unit,
    onDeleteNote: () -> Unit,
    onDismiss: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Card(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Row(
                    modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Informacje o notatce: \n${note.noteType.toNoteDrawableType().type}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    IconButton(
                        onClick = { },
                    ) {
                        Icon(
                            painter = painterResource(com.notesmakers.common_ui.R.drawable.keep),
                            contentDescription = null
                        )
                    }
                }
                NoteInfoItem(
                    label = "Data utworzenia:",
                    value = note.createdAt.format(DateTimeFormatter.ofPattern(PATTERN))
                )
                NoteInfoItem(
                    label = "Data ostatniej modyfikacji:",
                    value = note.createdAt.format(DateTimeFormatter.ofPattern(PATTERN))
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = onEditNote,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Edytuj")
                    }
                    Button(
                        onClick = onDeleteNote,
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(text = "Usuń")
                    }
                }
            }
        }
    }
}

@Composable
private fun NoteInfoItem(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun ScrollToTopButton(
    modifier: Modifier,
    showButton: Boolean,
    listState: LazyStaggeredGridState,
) {
    val coroutineScope = rememberCoroutineScope()
    AnimatedVisibility(
        visible = showButton,
        modifier = modifier,
        enter = getEnterScrollTransition(),
        exit = getExitScrollTransition(),
    ) {
        BaseIconButton(
            painterResource = com.notesmakers.common_ui.R.drawable.expand_less,
            modifier = Modifier.size(30.dp),
            tint = MaterialTheme.colorScheme.primary,
            onClick = {
                coroutineScope.launch {
                    listState.scrollToItem(index = 0)
                }
            },
        )
    }
}

@Composable
private fun ItemNote(
    title: String,
    textContent: String,
    dateTime: String,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(20.dp))
            .background(color = (MaterialTheme.colorScheme.tertiaryContainer).copy(alpha = 0.8f))
            .combinedClickable(
                onClick = { onClick() },
                onLongClick = { onLongClick() },
            )
            .padding(12.dp)
    ) {
        Row {
            Text(
                text = title,
                fontSize = 18.sp,
                maxLines = 1,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )
        }
        Text(
            modifier = Modifier
                .fillMaxWidth(),
            text = textContent,
            fontSize = 14.sp,
            maxLines = 7,
            fontWeight = FontWeight.Light,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            Text(text = dateTime, fontSize = 12.sp)
        }
    }
}

fun getTextContent() =
    "Jaja nosy niezasnąć moździerza Pani Opatrzonych roli Gors kapeluszach. Mój trzy boku Białopiotrowiczowi wiem inne woń dwie Wara. Batystową przyjezdny gęstość wstydu zwycięsca włościan dział Szabli świat wejrzeniem dzień. Stare przyszłą kołnierzy Jaszczurem wyraz pomniejszy wróciwszy pasem powrócił serce organ Godna Jutro. Dwoma Nauka dojeżdżaczów dziad pokrewieństwem mogę Worończańskim. Woń Najpiękniejszego Rzeczypospolitéj najpiękniejszéj żyt izba bór dali piki rogi. Ludu Jego uczy psem najpiękniejszéj krew wnet znak Rzeczypospolitéj Najpiękniejszego.".let {
        it.substring(0, (70..200).random())
    }

fun getTitleContent() =
    listOf("title", "twotwotwotwotwotwotwo", "three", "four", "five").asSequence().shuffled()
        .find { true }
