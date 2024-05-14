package com.notesmakers.noteapp.presentation.home.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.notesmakers.common_ui.R
import com.notesmakers.noteapp.presentation.notes.creation.NoteMode
import com.notesmakers.ui.composables.buttons.BaseIconButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    accountIconAction: () -> Unit,
    navToNote: (noteMode: NoteMode) -> Unit,
    userIsLoggedIn: Boolean,
    logout: () -> Unit,
) {
    TopAppBar(colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.background,
        titleContentColor = MaterialTheme.colorScheme.onBackground,
    ), title = {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Note it")
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (userIsLoggedIn.not()) {
                    BaseIconButton(
                        onClick = accountIconAction,
                        painterResource = com.notesmakers.common_ui.R.drawable.no_accounts
                    )
                }
                BaseDropdownMenu(
                    userIsLoggedIn = userIsLoggedIn,
                    painterResource = R.drawable.more_vert,
                    navToNote = { navToNote(it) },
                    logout = logout
                )
            }
        }
    })
}

@Composable
fun BaseDropdownMenu(
    userIsLoggedIn: Boolean,
    navToNote: (noteMode: NoteMode) -> Unit,
    logout: () -> Unit,
    @DrawableRes painterResource: Int,
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }

    fun closeMenu() {
        isExpanded = false
    }
    Row {
        BaseIconButton(
            onClick = { isExpanded = !isExpanded },
            painterResource = painterResource
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Create Quick Note") },
                onClick = {
                    navToNote(NoteMode.QUICK_NOTE)
                }
            )
            DropdownMenuItem(
                text = { Text("Create Paint Note") },
                onClick = {
                    navToNote(NoteMode.PAINT_NOTE)
                }
            )
            HorizontalDivider(
                color = Color.LightGray.copy(alpha = 0.5f),
                modifier = Modifier.padding(vertical = 4.dp)
            )
            if (userIsLoggedIn) {
                DropdownMenuItem(
                    text = { Text("Logout") },
                    onClick = {
                        closeMenu()
                        logout()
                    }
                )
            }
        }
    }
}