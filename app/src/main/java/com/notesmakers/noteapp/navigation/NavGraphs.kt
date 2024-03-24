package com.notesmakers.noteapp.navigation

import com.notesmakers.auth_ui.destinations.LoginScreenDestination
import com.notesmakers.home_ui.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraphs {

    val main = object : NavGraphSpec {
        override val route = "main"

        override val startRoute = LoginScreenDestination

        override val destinationsByRoute = listOf(
            HomeScreenDestination,
            LoginScreenDestination,
        ).associateBy { it.route }
    }
}
