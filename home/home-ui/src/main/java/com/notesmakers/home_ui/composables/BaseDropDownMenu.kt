package com.notesmakers.home_ui.composables

import android.widget.Toast
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.notesmakers.common_ui.buttons.BaseIconButton
import com.notesmakers.home_ui.R

@Composable
fun BaseDropdownMenu() {
    val context = LocalContext.current
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Row {
        BaseIconButton(
            onClick = { isExpanded = !isExpanded },
            painterResource = R.drawable.more_vert
        )
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Load") },
                onClick = { Toast.makeText(context, "Load", Toast.LENGTH_SHORT).show() }
            )
            DropdownMenuItem(
                text = { Text("Save") },
                onClick = { Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show() }
            )
        }
    }
}