package com.colledk.profile.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.colledk.profile.ui.ProfileViewModel
import com.colledk.profile.ui.compose.ProfilePane
import com.colledk.profile.ui.compose.ProfileScreen

const val profileScreenRoute = "profilescreen_route"
const val profilePaneRoute = "profilepage_route"

fun NavGraphBuilder.profileScreen() {
    composable(
        route = "$profileScreenRoute/{userId}",
        arguments = listOf(
            navArgument("userId") { type = NavType.StringType }
        )
    ) { backStackEntry ->
        ProfileScreen(userId = backStackEntry.arguments?.getString("userId").orEmpty())
    }
}

fun NavGraphBuilder.profilePane() {
    composable(route = profilePaneRoute) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState().also {
            viewModel.getUser()
        }
        ProfilePane(
            isEditable = true,
            uiState = uiState,
            onEditProfile = {},
            onCreateChat = {}
        )
    }
}

fun NavController.navigateToProfilePane(navOptions: NavOptions? = null) {
    this.navigate(route = profilePaneRoute, navOptions = navOptions)
}

fun NavController.navigateToProfileScreen(userId: String, navOptions: NavOptions? = null) {
    this.navigate(route = "$profileScreenRoute/$userId", navOptions = navOptions)
}