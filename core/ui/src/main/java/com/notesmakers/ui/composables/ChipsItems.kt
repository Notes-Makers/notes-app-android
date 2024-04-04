package com.notesmakers.ui.composables

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
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

@Composable
fun SelectedChipItem(text: String, selected: Boolean, onClick: () -> Unit = {}) {
    FilterChip(
        selected = selected,
        modifier = Modifier.padding(end = 4.dp),
        onClick = onClick,
        label = { Text(text) },
    )
}