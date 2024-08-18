package com.colledk.onboarding.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.colledk.onboarding.ui.OnboardingPane
import com.colledk.onboarding.ui.ProfileSetupPane

fun NavController.navigateToOnboarding(navOptions: NavOptions? = null) {
    this.navigate(route = Login, navOptions = navOptions)
}

fun NavController.navigateToProfileSetup(navOptions: NavOptions? = null) {
    this.navigate(route = ProfileSetup, navOptions = navOptions)
}

fun NavGraphBuilder.onboardingGraph(
    navHostController: NavHostController,
    onFinishOnboarding: () -> Unit
) {
    navigation<Onboarding>(
        startDestination = Login
    ) {
        composable<Login> {
            OnboardingPane(
                onGoToSetup = {
                    navHostController.navigateToProfileSetup()
                },
                onGoToFrontpage = onFinishOnboarding
            )
        }
        composable<ProfileSetup> {
            ProfileSetupPane {
                onFinishOnboarding()
            }
        }
    }
}

