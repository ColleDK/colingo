package com.colledk.colingo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.colledk.colingo.ColingoAppState
import com.colledk.colingo.compose.ChatPane
import com.colledk.colingo.compose.ExplorePane
import com.colledk.colingo.compose.HomePane
import com.colledk.colingo.compose.ProfilePane
import com.colledk.colingo.compose.SettingsPane

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
    startDestination: String = homePaneRoute
) {
    val navController = appState.navController
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        homePane()
        explorePane()
        chatPane()
        profilePane()
        settingsPane()
    }
}