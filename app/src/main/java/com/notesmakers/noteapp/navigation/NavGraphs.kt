package com.notesmakers.noteapp.navigation

import com.notesmakers.noteapp.destinations.HomeScreenDestination
import com.notesmakers.notes_ui.destinations.TestScreenDestination
import com.ramcosta.composedestinations.spec.NavGraphSpec

object NavGraphs {

    val main = object : NavGraphSpec {
        override val route = "main"

        override val startRoute = HomeScreenDestination

        override val destinationsByRoute = listOf(
            HomeScreenDestination,
            TestScreenDestination,
        ).associateBy { it.route }
    }
//    val estimation = object : NavGraphSpec {
//        override val route = "estimation"
//
//        override val startRoute = EstimationScreenDestination
//
//        override val destinationsByRoute = listOf(
//            EstimationScreenDestination,
//            OpenBottomSheetDestination,
//            SaveBottomSheetDestination,
//            SubgenusBottomSheetDestination,
//            ResetBottomSheetDestination,
//        ).associateBy { it.route }
//    }
}
