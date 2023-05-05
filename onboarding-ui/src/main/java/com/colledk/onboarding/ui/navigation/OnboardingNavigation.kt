package com.colledk.onboarding.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.colledk.onboarding.ui.compose.OnboardingScreen
import com.colledk.onboarding.ui.compose.signup.CreateUserScreen

private const val onboardingGraphRoutePattern = "onboarding_graph"
const val onboardingRoute = "onboarding_route"
const val signUpRoute = "onboarding_signup_route"

fun NavController.navigateToOnboardingGraph(navOptions: NavOptions? = null) {
    this.navigate(route = onboardingGraphRoutePattern, navOptions = navOptions)
}

fun NavController.navigateToSignUp(navOptions: NavOptions? = null) {
    this.navigate(route = signUpRoute, navOptions = navOptions)
}

fun NavGraphBuilder.onboardingGraph(
    navHostController: NavHostController
) {
    navigation(
        route = onboardingGraphRoutePattern,
        startDestination = onboardingRoute
    ) {
        composable(route = onboardingRoute) {
            OnboardingScreen(
                navController = navHostController
            )
        }

        composable(route = signUpRoute) {
            CreateUserScreen()
        }
    }
}