package com.colledk.onboarding.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navOptions
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
    currentUserId: String?,
    onFinishOnboarding: () -> Unit
) {
    navigation<Onboarding>(
        startDestination = Login
    ) {
        composable<Login> {
            LaunchedEffect(key1 = currentUserId) {
                if (currentUserId != null) {
                    onFinishOnboarding()
                }
            }

            OnboardingPane(
                onGoToSetup = {
                    navHostController.navigateToProfileSetup(
                        navOptions = navOptions {
                            popUpTo(navHostController.graph.id) {
                                inclusive = true
                            }
                        }
                    )
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

