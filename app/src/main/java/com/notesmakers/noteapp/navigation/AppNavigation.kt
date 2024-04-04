package com.notesmakers.noteapp.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.notesmakers.ui.animations.navEnterTransition
import com.notesmakers.ui.animations.navExitTransition
import com.notesmakers.ui.animations.popEnterTransition
import com.notesmakers.ui.animations.popExitTransition
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.animations.defaults.RootNavGraphDefaultAnimations
import com.ramcosta.composedestinations.animations.rememberAnimatedNavHostEngine
import com.ramcosta.composedestinations.navigation.DependenciesContainerBuilder
import com.ramcosta.composedestinations.spec.NavGraphSpec
import com.ramcosta.composedestinations.spec.NavHostEngine
import com.ramcosta.composedestinations.spec.Route

@OptIn(ExperimentalMaterialNavigationApi::class)
@ExperimentalAnimationApi
@Composable
internal fun AppNavigation(
    modifier: Modifier = Modifier,
    engine: NavHostEngine = rememberAnimatedNavHostEngine(
        rootDefaultAnimations = RootNavGraphDefaultAnimations(
            enterTransition = {
                navEnterTransition()
            },
            exitTransition = {
                navExitTransition()
            },
            popEnterTransition = {
                popEnterTransition()
            },
            popExitTransition = {
                popExitTransition()
            },
        )
    ),
    navController: NavHostController,
    onOpenSettings: @Composable DependenciesContainerBuilder<*>.() -> Unit = {},
    startRoute: Route,
    navGraph: NavGraphSpec,
) {
    DestinationsNavHost(
        engine = engine,
        navController = navController,
        navGraph = navGraph,
        modifier = modifier,
        startRoute = startRoute,
        dependenciesContainerBuilder = onOpenSettings
    )
}