package com.notesmakers.noteapp.presentation.main

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import com.notesmakers.noteapp.presentation.base.NoteAppSnackbar
import com.notesmakers.noteapp.presentation.base.SnackbarHandler
import com.notesmakers.noteapp.navigation.AppNavigation
import com.notesmakers.noteapp.navigation.NavGraphs
import com.notesmakers.noteapp.navigation.RootNavGraph
import com.ramcosta.composedestinations.navigation.dependency

@OptIn(
    ExperimentalComposeUiApi::class, ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class, ExperimentalMaterial3Api::class
)
@Composable
fun MainScreen() {
    val sheetState = rememberModalBottomSheetState()
    val navController = rememberNavController()
    val bottomSheetNavigator = rememberBottomSheetNavigator()
    val scope = rememberCoroutineScope()
    val snackbarState = SnackbarHandler(scope)
    navController.navigatorProvider.addNavigator(bottomSheetNavigator)
    Box(modifier = Modifier.fillMaxSize()) {
        ModalBottomSheetLayout(
            modifier = Modifier.navigationBarsPadding(),
            bottomSheetNavigator = bottomSheetNavigator,
            sheetElevation = 0.dp
        ) {
            AppNavigation(
                modifier = Modifier.fillMaxSize(),
                navController = navController,
                startRoute = NavGraphs.main,
                navGraph = RootNavGraph,
                onOpenSettings = {
                    dependency(sheetState)
                    dependency(snackbarState)
                }
            )
        }
        NoteAppSnackbar(
            modifier = Modifier
                .navigationBarsPadding()
                .align(Alignment.BottomCenter),
            snackbarHandler = snackbarState,
            navController = navController
        )
    }
}