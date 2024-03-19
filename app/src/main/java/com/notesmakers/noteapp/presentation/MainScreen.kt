package com.notesmakers.noteapp.presentation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.notesmakers.noteapp.navigation.AppNavigation
import com.notesmakers.noteapp.navigation.NavGraphs
import com.notesmakers.noteapp.navigation.RootNavGraph

@OptIn(ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    AppNavigation(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startRoute = NavGraphs.main,
        navGraph = RootNavGraph
    )
}