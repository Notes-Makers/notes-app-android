package com.notesmakers.noteapp.navigation

import androidx.compose.ui.ExperimentalComposeUiApi
import com.ramcosta.composedestinations.spec.DestinationSpec
import com.ramcosta.composedestinations.spec.NavGraphSpec

@ExperimentalComposeUiApi
object RootNavGraph : NavGraphSpec {

    override val route = "root"

    override val destinationsByRoute = emptyMap<String, DestinationSpec<*>>()

    override val startRoute = NavGraphs.main

    override val nestedNavGraphs = listOf(
        NavGraphs.main,
    )
}