package com.colledk.colingo.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.colledk.colingo.ColingoAppState
import com.colledk.colingo.compose.ColingoApp
import com.colledk.colingo.compose.HomeScreen
import com.colledk.onboarding.ui.navigation.onboardingGraph
import com.colledk.onboarding.ui.navigation.signUpRoute

const val homeScreenRoute = "homepage_route"

// TODO move home screen to separate module
fun NavGraphBuilder.homeScreen() {
    composable(route = homeScreenRoute) {
        HomeScreen()
    }
}

fun NavController.navigateToHomeScreen(navOptions: NavOptions? = null) {
    this.navigate(route = homeScreenRoute, navOptions = navOptions)
}

@Composable
fun ColingoNavHost(
    appState: ColingoAppState,
    startDestination: String = homeScreenRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homeScreen()

        onboardingGraph(navHostController = navController)
    }
}