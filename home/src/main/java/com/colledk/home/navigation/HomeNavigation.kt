package com.colledk.home.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.colledk.home.ui.HomeViewModel
import com.colledk.home.ui.compose.HomePane
import com.colledk.home.ui.uistates.HomeUiState
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

const val homePaneRoute = "homepage_route"

fun NavGraphBuilder.homePane() {
    composable(route = homePaneRoute) {
        val viewModel: HomeViewModel = hiltViewModel()
        val posts = viewModel.posts.collectAsLazyPagingItems()
        val user by viewModel.currentUser.collectAsState()
        val sorting by viewModel.sorting.collectAsState()

        val snackbarHostState = remember { SnackbarHostState() }

        LaunchedEffect(key1 = posts.loadState) {
            if (posts.loadState.refresh is LoadState.Error) {
                snackbarHostState.showSnackbar(
                    ((posts.loadState.refresh as? LoadState.Error)?.error?.message ?:
                    "Something went wrong. Please try again") + Firebase.auth.uid
                )
            }
        }

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
            Scaffold(
                snackbarHost = {
                    SnackbarHost(hostState = snackbarHostState)
                }
            ) { padding ->
                HomePane(
                    uiState = uiState,
                    onLikePost = {
                        viewModel.likePost(it, uiState.currentUser.id)
                    },
                    onRemoveLike = {
                        viewModel.removeLike(it, uiState.currentUser.id)
                    },
                    onRefresh = posts::refresh,
                    onCreatePost = viewModel::createPost,
                    onSort = viewModel::updateSorting,
                    formatNumber = viewModel::formatNumber,
                    modifier = Modifier.padding(padding)
                )
            }
        }
    }
}

fun NavController.navigateToHomePane(navOptions: NavOptions? = null) {
    this.navigate(route = homePaneRoute, navOptions = navOptions)
}