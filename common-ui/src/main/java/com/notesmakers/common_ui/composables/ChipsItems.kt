package com.notesmakers.common_ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ChipItem(text: String, onClick: () -> Unit = {}) {
    SuggestionChip(
        modifier = Modifier.padding(end = 4.dp),
        onClick = onClick,
        label = { Text(text) },
    )
}