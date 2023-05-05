package com.colledk.colingo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.colledk.colingo.ColingoAppState
import com.colledk.colingo.compose.ColingoApp
import com.colledk.onboarding.ui.navigation.onboardingGraph

private const val appRoute = "colingo_route"

fun NavGraphBuilder.appScreen() {
    composable(route = appRoute) {
        ColingoApp()
    }
}

@Composable
fun ColingoNavHost(
    appState: ColingoAppState,
    startDestination: String = appRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        appScreen()

        onboardingGraph(navHostController = navController)
    }
}