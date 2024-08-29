package com.colledk.chat.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.colledk.chat.ui.ChatViewModel
import com.colledk.chat.ui.compose.ChatScreen

fun NavController.navigateToChat(navOptions: NavOptions? = null) {
    this.navigate(route = Chat, navOptions = navOptions)
}

fun NavGraphBuilder.chatPane() {
    composable<Chat> {
        val viewModel: ChatViewModel = hiltViewModel()

        LaunchedEffect(key1 = Unit) {
            viewModel.getChats()
            viewModel.getAiChats()
        }

        val error by viewModel.error.collectAsState(null)
        val uiState by viewModel.uiState.collectAsState()
        val selectedChat by viewModel.selectedChat.collectAsStateWithLifecycle()
        val selectedPane by viewModel.selectedPane.collectAsStateWithLifecycle()

        ChatScreen(
            state = uiState,
            error = error,
            selectedChat = selectedChat,
            deleteChat = viewModel::deleteChat,
            sendMessage = viewModel::sendMessage,
            deleteAiChat = viewModel::deleteAiChat,
            sendAiMessage = viewModel::sendAiMessage,
            selectChat = viewModel::selectChat,
            selectedPage = selectedPane,
            selectPage = viewModel::selectPane
        )
    }
}