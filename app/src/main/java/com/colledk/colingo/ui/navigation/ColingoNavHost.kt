package com.colledk.colingo.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navOptions
import com.colledk.chat.navigation.chatPane
import com.colledk.colingo.ui.ColingoAppState
import com.colledk.colingo.ui.SettingsViewModel
import com.colledk.colingo.ui.compose.SettingsPane
import com.colledk.community.navigation.explorePane
import com.colledk.home.navigation.homePane
import com.colledk.home.navigation.navigateToHomePane
import com.colledk.onboarding.navigation.Onboarding
import com.colledk.onboarding.navigation.navigateToOnboarding
import com.colledk.onboarding.navigation.onboardingGraph
import com.colledk.profile.navigation.profilePane
import com.colledk.profile.navigation.profileScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

fun NavGraphBuilder.settingsPane(onLogOut: () -> Unit) {
    composable<Settings> {
        val viewModel: SettingsViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState()
        val selectedSetting by viewModel.selectedSetting.collectAsStateWithLifecycle()

        SettingsPane(
            onLogOut = onLogOut,
            onSwitchLanguage = viewModel::switchLanguages,
            settingsState = uiState,
            retrieveSettings = viewModel::getSettings,
            updateSetting = viewModel::updateSetting,
            selectedSetting = selectedSetting,
            selectSetting = viewModel::selectSetting
        )
    }
}

fun NavController.navigateToSettingsPane(navOptions: NavOptions? = null) {
    this.navigate(route = Settings, navOptions = navOptions)
}

@Composable
fun ColingoNavHost(
    appState: ColingoAppState,
    modifier: Modifier = Modifier,
    startDestination: Any = Onboarding
) {
    val navController = appState.navController
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(700)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(700)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(700)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(700)
            )
        }
    ) {
        onboardingGraph(navHostController = navController, currentUserId = appState.currentUserId) {
            navController.navigateToHomePane(
                navOptions = navOptions {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            )
        }
        homePane()
        explorePane()
        chatPane()
        profilePane()
        profileScreen()
        settingsPane {
            navController.navigateToOnboarding(
                navOptions = navOptions {
                    popUpTo(navController.graph.id) {
                        inclusive = true
                    }
                }
            ).also {
                Firebase.auth.signOut()
            }
        }
    }
}