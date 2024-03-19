package com.notesmakers.notes_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun TestScreen(
    navigator: DestinationsNavigator
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(text = "TestScreen")
    }
}