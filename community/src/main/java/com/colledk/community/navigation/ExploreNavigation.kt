package com.colledk.community.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.colledk.community.ui.ExploreViewModel
import com.colledk.community.ui.compose.ExplorePane

fun NavGraphBuilder.explorePane() {
    composable<Community> {
        val viewModel: ExploreViewModel = hiltViewModel()
        val users = viewModel.users.collectAsLazyPagingItems()
        val filters by viewModel.filters.collectAsState()
        val currentUser by viewModel.currentUser.collectAsState()
        val selectedUser by viewModel.selectedUser.collectAsStateWithLifecycle()
        val selectedPage by viewModel.selectedPane.collectAsStateWithLifecycle()

        LaunchedEffect(key1 = filters) {
            users.refresh()
        }

        ExplorePane(
            users = users,
            onCreateAiChat = viewModel::createAiChat,
            selectFilters = viewModel::updateFilters,
            currentFilters = filters,
            userLanguages = currentUser?.languages?.map { it.language.language }.orEmpty(),
            onCreateChat = viewModel::createChat,
            selectUser = viewModel::selectUser,
            selectedUser = selectedUser,
            selectedPage = selectedPage,
            selectPage = viewModel::selectPane
        )
    }
}

fun NavController.navigateToExplorePane(navOptions: NavOptions? = null) {
    this.navigate(route = Community, navOptions = navOptions)
}