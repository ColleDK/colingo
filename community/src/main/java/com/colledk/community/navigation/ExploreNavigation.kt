package com.colledk.community.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.colledk.community.ui.ExploreViewModel
import com.colledk.community.ui.compose.ExplorePane
import com.colledk.user.domain.model.User

const val explorePaneRoute = "explorepage_route"

fun NavGraphBuilder.explorePane(onUserClicked: (user: User) -> Unit) {
    composable(route = explorePaneRoute) {
        val viewModel: ExploreViewModel = hiltViewModel()
        val users = viewModel.users.collectAsLazyPagingItems()
        ExplorePane(users = users, onUserClicked = onUserClicked)
    }
}

fun NavController.navigateToExplorePane(navOptions: NavOptions? = null) {
    this.navigate(route = explorePaneRoute, navOptions = navOptions)
}