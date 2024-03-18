package com.notesmakers.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.notesmakers.noteapp.navigation.NavGraphs
import com.notesmakers.noteapp.ui.theme.NoteAppTheme
import com.ramcosta.composedestinations.DestinationsNavHost

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme {
                DestinationsNavHost(navGraph = NavGraphs.main)
            }
        }
    }
}
