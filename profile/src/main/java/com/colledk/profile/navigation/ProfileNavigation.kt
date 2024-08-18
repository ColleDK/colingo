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
import androidx.navigation.toRoute
import com.colledk.profile.ui.ProfileViewModel
import com.colledk.profile.ui.compose.ProfilePane
import com.colledk.profile.ui.compose.ProfileScreen

fun NavGraphBuilder.profileScreen() {
    composable<UserProfile> { backStackEntry ->
        ProfileScreen(userId = backStackEntry.toRoute<UserProfile>().userId)
    }
}

fun NavGraphBuilder.profilePane() {
    composable<Profile> {
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
    this.navigate(route = Profile, navOptions = navOptions)
}

fun NavController.navigateToProfileScreen(userId: String, navOptions: NavOptions? = null) {
    this.navigate(route = UserProfile(userId = userId), navOptions = navOptions)
}