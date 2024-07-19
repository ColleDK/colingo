package com.colledk.home.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.colledk.home.ui.HomeViewModel
import com.colledk.home.ui.compose.HomePane
import com.colledk.home.ui.uistates.HomeUiState

const val homePaneRoute = "homepage_route"

fun NavGraphBuilder.homePane() {
    composable(route = homePaneRoute) {
        val viewModel: HomeViewModel = hiltViewModel()
        val posts = viewModel.posts.collectAsLazyPagingItems()
        val user by viewModel.currentUser.collectAsState()
        val sorting by viewModel.sorting.collectAsState()

        val uiState by remember(posts, user, sorting) {
            mutableStateOf(
                user?.let { user -> HomeUiState(
                    posts = posts,
                    currentUser = user,
                    currentSort = sorting
                ) }
            )
        }

        uiState?.let { uiState ->
            HomePane(
                uiState = uiState,
                onRefresh = posts::refresh,
                onCreatePost = viewModel::createPost,
                onLikePost = {
                    viewModel.likePost(it, uiState.currentUser.id)
                },
                onRemoveLike = {
                    viewModel.removeLike(it, uiState.currentUser.id)
                },
                onSort = viewModel::updateSorting
            )
        }
    }
}

fun NavController.navigateToHomePane(navOptions: NavOptions? = null) {
    this.navigate(route = homePaneRoute, navOptions = navOptions)
}