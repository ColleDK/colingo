package com.colledk.chat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.colledk.chat.ui.compose.ChatScreen

fun NavController.navigateToChat(navOptions: NavOptions? = null) {
    this.navigate(route = Chat, navOptions = navOptions)
}

fun NavGraphBuilder.chatPane() {
    composable<Chat> {
        ChatScreen()
    }
}