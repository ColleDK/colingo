package com.colledk.profile.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import com.colledk.profile.ui.ProfileViewModel
import com.colledk.profile.ui.compose.ProfilePane

const val profilePaneRoute = "profilepage_route"

fun NavGraphBuilder.profilePane() {
    composable(route = profilePaneRoute) {
        val viewModel: ProfileViewModel = hiltViewModel()
        val uiState by viewModel.uiState.collectAsState().also {
            viewModel.getUser()
        }
        ProfilePane(uiState = uiState, onEditProfile = {}, onPictureAdded = viewModel::addProfilePicture)
    }
}

fun NavController.navigateToProfilePane(navOptions: NavOptions? = null) {
    this.navigate(route = profilePaneRoute, navOptions = navOptions)
}