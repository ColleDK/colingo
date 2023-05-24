package com.colledk.chat.ui.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.colledk.chat.ui.compose.ChatScreen

private const val chatGraphRoutePattern = "chat_graph"
const val chatRoute = "chat_route"

fun NavController.navigateToChatGraph(navOptions: NavOptions? = null) {
    this.navigate(route = chatGraphRoutePattern, navOptions = navOptions)
}

fun NavGraphBuilder.chatGraph(
    navHostController: NavHostController
) {
    navigation(
        route = chatGraphRoutePattern,
        startDestination = chatRoute
    ) {
        composable(route = chatRoute) {
            ChatScreen(
                navController = navHostController
            )
        }

    }
}