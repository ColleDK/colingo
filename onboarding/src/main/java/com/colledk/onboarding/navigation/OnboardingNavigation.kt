package com.colledk.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.colledk.onboarding.ui.OnboardingPane
import com.colledk.onboarding.ui.ProfileSetupPane

const val onboardingGraphRoute = "onboarding_graph"
private const val onboardingPaneRoute = "onboarding_route"
private const val profileSetupPaneRoute = "onboarding_profile_setup_route"
fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    this.navigate(route = onboardingPaneRoute, navOptions = navOptions)
}

fun NavController.navigateToProfileSetup(navOptions: NavOptions? = null) {
    this.navigate(route = profileSetupPaneRoute, navOptions = navOptions)
}

fun NavGraphBuilder.onboardingGraph(
    navHostController: NavHostController,
    onFinishOnboarding: () -> Unit
) {
    navigation(
        startDestination = onboardingPaneRoute,
        route = onboardingGraphRoute
    ) {
        composable(onboardingPaneRoute) {
            OnboardingPane {
                navHostController.navigateToProfileSetup()
            }
        }
        composable(profileSetupPaneRoute) {
            ProfileSetupPane {
                onFinishOnboarding()
            }
        }
    }
}

