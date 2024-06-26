package com.colledk.chat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.colledk.chat.ui.ChatScreen

const val chatGraphRoute = "chat_graph"
private const val chatPaneRoute = "chat_route"
private const val chatDetailPaneRoute = "chat_detail_route"

fun NavController.navigateToChat(navOptions: NavOptions? = null) {
    this.navigate(route = chatPaneRoute, navOptions = navOptions)
}

fun NavGraphBuilder.chatGraph(
    navHostController: NavHostController
) {
    navigation(
        startDestination = chatPaneRoute,
        route = chatGraphRoute
    ) {
        composable(chatPaneRoute) {
            ChatScreen()
        }

    }
}