package com.notesmakers.noteapp.features.notes.presentation.paintnote

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notesmakers.noteapp.features.destinations.PaintNoteScreenDestination
import com.notesmakers.noteapp.features.notes.data.Note
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PaintNoteScreen(
    navigator: DestinationsNavigator,
    noteId: String,
    paintNoteViewModel: PaintNoteViewModel = koinViewModel { parametersOf(noteId) }
) {
    val noteState = paintNoteViewModel.noteState.collectAsStateWithLifecycle().value
    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                ),
                title = {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        BaseIconButton(
                            onClick = { navigator.popBackStack() },
                            imageVector = Icons.Default.Clear
                        )
                        BaseIconButton(
                            onClick = { },
                            imageVector = Icons.Default.Share
                        )
                    }
                }
            )
        },
    ) { innerPadding ->
        noteState?.let {
            PaintNoteScreen(
                innerPadding = innerPadding,
                note = noteState,
                updatePageCount = {
                    paintNoteViewModel.updatePageCount(it)
                })
        }
    }
}

fun DestinationsNavigator.navToPaintNote(noteId: String) =
    navigate(PaintNoteScreenDestination(noteId))

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PaintNoteScreen(
    innerPadding: PaddingValues,
    note: Note,
    updatePageCount: (Int) -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { note.pageCount })
    var selectedTabIndex = pagerState.currentPage
    val coroutineScope = rememberCoroutineScope()
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                ScrollableTabRow(
                    selectedTabIndex = selectedTabIndex,
                    modifier = Modifier
                        .weight(1f)
                        .padding(0.dp),
                    edgePadding = 0.dp,
                    divider = {},
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            color = MaterialTheme.colorScheme.outlineVariant,
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                        )
                    }
                ) {
                    IntRange(
                        start = 1,
                        endInclusive = note.pageCount
                    ).forEachIndexed { index, item ->
                        Tab(
                            modifier = Modifier
                                .height(40.dp)
                                .width(110.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.background
                                ),
                            selected = index == selectedTabIndex,
                            onClick = {
                                selectedTabIndex = index
                                coroutineScope.launch { pagerState.animateScrollToPage(index) }
                            }) {
                            Text(
                                text = "Page $item",
                                modifier = Modifier.padding(4.dp),
                            )
                        }
                    }
                }
                BaseIconButton(
                    onClick = { updatePageCount(note.pageCount + 1) },
                    imageVector = Icons.Default.Add
                )
            }
            HorizontalPager(
                userScrollEnabled = false,
                state = pagerState,
                modifier = Modifier.weight(1f),
            ) {
                PaintSpace(
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}