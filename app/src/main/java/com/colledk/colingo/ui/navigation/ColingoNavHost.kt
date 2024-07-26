package com.colledk.colingo.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.colledk.chat.navigation.chatGraph
import com.colledk.colingo.ui.ColingoAppState
import com.colledk.colingo.ui.SettingsViewModel
import com.colledk.colingo.ui.compose.SettingsPane
import com.colledk.community.navigation.explorePane
import com.colledk.home.navigation.homePane
import com.colledk.onboarding.navigation.navigateToOnboarding
import com.colledk.onboarding.navigation.onboardingGraph
import com.colledk.onboarding.navigation.onboardingGraphRoute
import com.colledk.profile.navigation.profilePane
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

const val settingsPaneRoute = "settingspage_route"

fun NavGraphBuilder.settingsPane(onLogOut: () -> Unit) {
    composable(route = settingsPaneRoute) {
        val viewModel: SettingsViewModel = hiltViewModel()
        SettingsPane(onLogOut = onLogOut, onSwitchLanguage = viewModel::switchLanguages)
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
        onboardingGraph(navHostController = navController) {
            appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
        }
        homePane()
        explorePane()
        chatGraph(navHostController = navController)
        profilePane()
        settingsPane {
            navController.navigateToOnboarding(/* TODO Pop backstack */).also {
                Firebase.auth.signOut()
            }
        }
    }
}