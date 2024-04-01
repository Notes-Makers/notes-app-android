package com.notesmakers.home_ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.notesmakers.ui.composables.buttons.BaseIconButton
import com.notesmakers.ui.composables.menu.BaseDropdownMenu
import com.notesmakers.home_ui.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseTopAppBar(
    accountIconAction: () -> Unit,
) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        title = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Note it")
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    BaseIconButton(
                        onClick = accountIconAction,
                        painterResource = R.drawable.no_accounts
                    )
                    BaseDropdownMenu(painterResource = R.drawable.more_vert)
                }
            }
        }
    )
}