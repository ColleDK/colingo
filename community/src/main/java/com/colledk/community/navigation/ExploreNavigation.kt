package com.colledk.community.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.colledk.community.ui.ExploreViewModel
import com.colledk.community.ui.compose.ExplorePane
import timber.log.Timber

const val explorePaneRoute = "explorepage_route"

fun NavGraphBuilder.explorePane() {
    composable(route = explorePaneRoute) {
        val viewModel: ExploreViewModel = hiltViewModel()
        val users = viewModel.users.collectAsLazyPagingItems()
        val filters by viewModel.filters.collectAsState()
        val currentUser by viewModel.currentUser.collectAsState()

        LaunchedEffect(key1 = filters) {
            users.refresh()
        }

        ExplorePane(
            users = users,
            onCreateAiChat = viewModel::createAiChat,
            selectFilters = viewModel::updateFilters,
            currentFilters = filters,
            userLanguages = currentUser?.languages?.map { it.language.language }.orEmpty()
        )
    }
}

fun NavController.navigateToExplorePane(navOptions: NavOptions? = null) {
    this.navigate(route = explorePaneRoute, navOptions = navOptions)
}