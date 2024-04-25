package com.notesmakers.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.notesmakers.noteapp.presentation.main.MainScreen
import com.notesmakers.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NoteAppTheme(dynamicColor = false) {
                MainScreen()
            }
        }
    }
}
