package com.notesmakers.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.paint.PaintSpace
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@OptIn(ExperimentalMaterial3Api::class)
@Destination
@Composable
fun PaintNoteScreen(navigator: DestinationsNavigator) {
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
        PaintSpace(
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun PaintNoteScreen(innerPadding: PaddingValues) {
    val fakeData = listOf(1, 2, 3, 4, 5)
    var selectedTabIndex by remember {
        mutableStateOf(0)
    }
    Box(
        modifier = Modifier
            .padding(innerPadding)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .padding(top = 40.dp)
        ) {
            DrawSpace()
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier
                    .weight(1f)
                    .padding(0.dp),
                edgePadding = 0.dp,
                divider = {},
                indicator = {}
            ) {
                fakeData.forEachIndexed { index, item ->
                    Tab(
                        modifier = Modifier
                            .height(40.dp)
                            .width(110.dp)
                            .border(
                                shape = RoundedCornerShape(2.dp),
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            .background(
                                color = if (index == selectedTabIndex) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
                            ),
                        selected = index == selectedTabIndex,
                        onClick = { selectedTabIndex = index }) {
                        Text(
                            text = "Page $item",
                            modifier = Modifier.padding(4.dp),
                            color = if (index != selectedTabIndex) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.background
                        )
                    }
                }
            }
            Column(
                modifier = Modifier
                    .height(40.dp)
                    .padding(start = 5.dp)
                    .width(55.dp)
                    .border(
                        shape = RoundedCornerShape(2.dp),
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    .padding(4.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                BaseIconButton(
                    onClick = {}, imageVector = Icons.Default.Add
                )
            }
        }

    }
}
