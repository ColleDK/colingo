package com.colledk.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.colledk.onboarding.ui.OnboardingPane

const val onboardingPaneRoute = "onboarding_route"

fun NavGraphBuilder.onboardingPane(onFinishOnboarding: () -> Unit) {
    composable(onboardingPaneRoute) {
        OnboardingPane {
            onFinishOnboarding()
        }
    }
}

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    this.navigate(route = onboardingPaneRoute, navOptions = navOptions)
}