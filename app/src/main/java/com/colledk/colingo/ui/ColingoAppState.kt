package com.colledk.colingo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.colledk.chat.navigation.chatGraphRoute
import com.colledk.chat.navigation.navigateToChat
import com.colledk.colingo.ui.navigation.TopLevelDestination
import com.colledk.colingo.ui.navigation.explorePaneRoute
import com.colledk.colingo.ui.navigation.homePaneRoute
import com.colledk.colingo.ui.navigation.navigateToExplorePane
import com.colledk.colingo.ui.navigation.navigateToHomePane
import com.colledk.colingo.ui.navigation.navigateToProfilePane
import com.colledk.colingo.ui.navigation.navigateToSettingsPane
import com.colledk.colingo.ui.navigation.profilePaneRoute
import com.colledk.colingo.ui.navigation.settingsPaneRoute

@Composable
fun rememberColingoAppState(
    navController: NavHostController = rememberNavController()
) : ColingoAppState {
    return remember(navController) {
        ColingoAppState(navController = navController)
    }
}

@Stable
data class ColingoAppState(
    val navController: NavHostController
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    /**
     * This does not work with nested graphs or safe-nav and should be rewritten
     */
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homePaneRoute -> TopLevelDestination.HOME
            explorePaneRoute -> TopLevelDestination.EXPLORE
            chatGraphRoute -> TopLevelDestination.CHAT
            profilePaneRoute -> TopLevelDestination.PROFILE
            settingsPaneRoute -> TopLevelDestination.SETTINGS
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    /**
     * This does not work with nested graphs and should be rewritten
     */
    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true

            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> navController.navigateToHomePane(navOptions = topLevelNavOptions)
            TopLevelDestination.EXPLORE -> navController.navigateToExplorePane(navOptions = topLevelNavOptions)
            TopLevelDestination.CHAT -> navController.navigateToChat(navOptions = topLevelNavOptions)
            TopLevelDestination.PROFILE -> navController.navigateToProfilePane(navOptions = topLevelNavOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettingsPane(navOptions = topLevelNavOptions)
        }
    }
}
