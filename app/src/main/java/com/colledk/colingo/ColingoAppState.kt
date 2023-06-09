package com.colledk.colingo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.colledk.chat.ui.navigation.chatRoute
import com.colledk.chat.ui.navigation.navigateToChatGraph
import com.colledk.colingo.navigation.TopLevelDestination
import com.colledk.colingo.navigation.homeScreenRoute
import com.colledk.colingo.navigation.navigateToHomeScreen

@Composable
fun rememberColingoAppState(
    navController: NavHostController = rememberNavController()
): ColingoAppState {
    return remember(
        navController
    ) {
        ColingoAppState(
            navController = navController
        )
    }
}

@Stable
class ColingoAppState(
    val navController: NavHostController
) {

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homeScreenRoute -> TopLevelDestination.HOME
            chatRoute -> TopLevelDestination.CHAT
            else -> null
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.values().asList()

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true

            restoreState = true
        }

        when (topLevelDestination) {
            TopLevelDestination.HOME -> {
                navController.navigateToHomeScreen(navOptions = topLevelNavOptions)
            }

            TopLevelDestination.SEARCH -> {}
            TopLevelDestination.PROFILE -> {}
            TopLevelDestination.CHAT -> {
                navController.navigateToChatGraph(navOptions = topLevelNavOptions)
            }
        }
    }
}