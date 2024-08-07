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
import com.colledk.chat.navigation.chatPaneRoute
import com.colledk.chat.navigation.navigateToChat
import com.colledk.colingo.ui.navigation.TopLevelDestination
import com.colledk.colingo.ui.navigation.navigateToSettingsPane
import com.colledk.colingo.ui.navigation.settingsPaneRoute
import com.colledk.community.navigation.explorePaneRoute
import com.colledk.community.navigation.navigateToExplorePane
import com.colledk.home.navigation.homePaneRoute
import com.colledk.home.navigation.navigateToHomePane
import com.colledk.profile.navigation.navigateToProfileScreen
import com.colledk.profile.navigation.profilePaneRoute
import com.colledk.profile.navigation.profileScreenRoute
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

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
    val currentUserId: String?
        get() = Firebase.auth.uid

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    /**
     * This does not work with nested graphs or safe-nav and should be rewritten
     */
    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when (currentDestination?.route) {
            homePaneRoute -> TopLevelDestination.HOME
            explorePaneRoute -> TopLevelDestination.EXPLORE
            chatPaneRoute -> TopLevelDestination.CHAT
            profilePaneRoute, profileScreenRoute -> TopLevelDestination.PROFILE
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
            TopLevelDestination.PROFILE -> navController.navigateToProfileScreen(userId = currentUserId.orEmpty(), navOptions = topLevelNavOptions)
            TopLevelDestination.SETTINGS -> navController.navigateToSettingsPane(navOptions = topLevelNavOptions)
        }
    }
}
