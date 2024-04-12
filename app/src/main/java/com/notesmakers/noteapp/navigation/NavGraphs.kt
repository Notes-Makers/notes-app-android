package com.notesmakers.noteapp.navigation

import com.notesmakers.auth_ui.destinations.LoginScreenDestination
import com.notesmakers.home_ui.destinations.HomeScreenDestination
import com.notesmakers.ui.destinations.NoteCreationScreenDestination
import com.notesmakers.ui.destinations.PaintNoteScreenDestination
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraphs {

    val main = object : NavGraphSpec {
        override val route = "main"

        override val startRoute = NoteCreationScreenDestination

        override val destinationsByRoute = listOf(
            HomeScreenDestination,
            LoginScreenDestination,
            PaintNoteScreenDestination,
            NoteCreationScreenDestination
        ).associateBy { it.route }
    }
}
