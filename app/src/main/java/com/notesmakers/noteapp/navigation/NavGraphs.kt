package com.notesmakers.noteapp.navigation

import com.notesmakers.noteapp.features.destinations.HomeScreenDestination
import com.notesmakers.noteapp.features.destinations.LoginScreenDestination
import com.notesmakers.noteapp.features.destinations.NoteCreationScreenDestination
import com.notesmakers.noteapp.features.destinations.PaintNoteScreenDestination
import com.notesmakers.noteapp.features.destinations.QuickNoteScreenDestination
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraphs {

    val main = object : NavGraphSpec {
        override val route = "main"

        override val startRoute = HomeScreenDestination

        override val destinationsByRoute = listOf(
            HomeScreenDestination,
            LoginScreenDestination,
            PaintNoteScreenDestination,
            NoteCreationScreenDestination,
            QuickNoteScreenDestination,
        ).associateBy { it.route }
    }
}
