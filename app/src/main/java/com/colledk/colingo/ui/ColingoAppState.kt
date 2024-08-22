package com.colledk.colingo.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.colledk.chat.navigation.Chat
import com.colledk.chat.navigation.navigateToChat
import com.colledk.colingo.ui.navigation.Settings
import com.colledk.colingo.ui.navigation.TopLevelDestination
import com.colledk.colingo.ui.navigation.navigateToSettingsPane
import com.colledk.community.navigation.Community
import com.colledk.community.navigation.navigateToExplorePane
import com.colledk.home.navigation.Home
import com.colledk.home.navigation.navigateToHomePane
import com.colledk.profile.navigation.Profile
import com.colledk.profile.navigation.UserProfile
import com.colledk.profile.navigation.navigateToProfileScreen
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
        get() = Firebase.auth.currentUser?.uid

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    private var lastDestination: TopLevelDestination? = null

    val currentTopLevelDestination: TopLevelDestination?
        @Composable get() = when {
            currentDestination?.hierarchy?.any { it.hasRoute(Profile::class) || it.hasRoute(UserProfile::class) } == true -> {
                lastDestination = TopLevelDestination.PROFILE
                lastDestination
            }
            currentDestination?.hierarchy?.any { it.hasRoute(Home::class) } == true -> {
                lastDestination = TopLevelDestination.HOME
                lastDestination
            }
            currentDestination?.hierarchy?.any { it.hasRoute(Community::class) } == true -> {
                lastDestination = TopLevelDestination.EXPLORE
                lastDestination
            }
            currentDestination?.hierarchy?.any { it.hasRoute(Chat::class) } == true -> {
                lastDestination = TopLevelDestination.CHAT
                lastDestination
            }
            currentDestination?.hierarchy?.any { it.hasRoute(Settings::class) } == true -> {
                lastDestination = TopLevelDestination.SETTINGS
                lastDestination
            }
            currentDestination == null -> {
                lastDestination
            }
            else -> {
                null
            }
        }

    val topLevelDestinations: List<TopLevelDestination> = TopLevelDestination.entries

    fun navigateToTopLevelDestination(topLevelDestination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            popUpTo(Home) {
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
