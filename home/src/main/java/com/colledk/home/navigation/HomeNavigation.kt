package com.colledk.home.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.colledk.home.ui.HomeViewModel
import com.colledk.home.ui.compose.HomePane

const val homePaneRoute = "homepage_route"

fun NavGraphBuilder.homePane() {
    composable(route = homePaneRoute) {
        val viewModel: HomeViewModel = hiltViewModel()
        val posts = viewModel.posts.collectAsLazyPagingItems()

        HomePane(
            posts = posts,
            onRefresh = posts::refresh,
            onCreatePost = viewModel::createPost
        )
    }
}

fun NavController.navigateToHomePane(navOptions: NavOptions? = null) {
    this.navigate(route = homePaneRoute, navOptions = navOptions)
}