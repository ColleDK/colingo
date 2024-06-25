package com.colledk.colingo.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.colledk.colingo.ui.ColingoAppState
import com.colledk.colingo.ui.compose.ChatPane
import com.colledk.colingo.ui.compose.ExplorePane
import com.colledk.colingo.ui.compose.HomePane
import com.colledk.colingo.ui.compose.ProfilePane
import com.colledk.colingo.ui.compose.SettingsPane
import com.colledk.onboarding.navigation.onboardingGraph
import com.colledk.onboarding.navigation.onboardingGraphRoute

// TODO temporary navigation, should be moved to separate modules
const val homePaneRoute = "homepage_route"
const val explorePaneRoute = "explorepage_route"
const val chatPaneRoute = "chatpage_route"
const val profilePaneRoute = "profilepage_route"
const val settingsPaneRoute = "settingspage_route"

fun NavGraphBuilder.homePane() {
    composable(route = homePaneRoute) {
        HomePane()
    }
}

fun NavController.navigateToHomePane(navOptions: NavOptions? = null) {
    this.navigate(route = homePaneRoute, navOptions = navOptions)
}

fun NavGraphBuilder.explorePane() {
    composable(route = explorePaneRoute) {
        ExplorePane()
    }
}

fun NavController.navigateToExplorePane(navOptions: NavOptions? = null) {
    this.navigate(route = explorePaneRoute, navOptions = navOptions)
}

fun NavGraphBuilder.chatPane() {
    composable(route = chatPaneRoute) {
        ChatPane()
    }
}

fun NavController.navigateToChatPane(navOptions: NavOptions? = null) {
    this.navigate(route = chatPaneRoute, navOptions = navOptions)
}

fun NavGraphBuilder.profilePane() {
    composable(route = profilePaneRoute) {
        ProfilePane()
    }
}

fun NavController.navigateToProfilePane(navOptions: NavOptions? = null) {
    this.navigate(route = profilePaneRoute, navOptions = navOptions)
}

fun NavGraphBuilder.settingsPane() {
    composable(route = settingsPaneRoute) {
        SettingsPane()
    }
}

fun NavController.navigateToSettingsPane(navOptions: NavOptions? = null) {
    this.navigate(route = settingsPaneRoute, navOptions = navOptions)
}

@Composable
fun ColingoNavHost(
    appState: ColingoAppState,
    startDestination: String = onboardingGraphRoute
) {
    val navController = appState.navController
    NavHost(
        modifier = Modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination,
        enterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        exitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.Start, tween(700)) },
        popEnterTransition = { slideIntoContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) },
        popExitTransition = { slideOutOfContainer(AnimatedContentTransitionScope.SlideDirection.End, tween(700)) }
    ) {
        onboardingGraph(navHostController = navController)
        homePane()
        explorePane()
        chatPane()
        profilePane()
        settingsPane()
    }
}