package com.notesmakers.ui.composables.menu

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.notesmakers.ui.composables.buttons.BaseIconButton

@Composable
fun BaseDropdownMenu(
    navToQuickNote: () -> Unit,
    navToPaintNote: () -> Unit,
    @DrawableRes painterResource: Int,
) {
    var isExpanded by remember {
        mutableStateOf(false)
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
                onClick = navToQuickNote
            )
            DropdownMenuItem(
                text = { Text("Create Paint Note") },
                onClick = navToPaintNote
            )
        }
    }
}