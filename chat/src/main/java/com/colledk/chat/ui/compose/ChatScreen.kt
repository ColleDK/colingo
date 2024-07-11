package com.colledk.chat.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.colledk.chat.ui.ChatViewModel
import com.colledk.chat.ui.uistates.ChatDetailUiState

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<String>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                val state by viewModel.uiState.collectAsState()

                LaunchedEffect(key1 = Unit) {
                    viewModel.getChats()
                    viewModel.getAiChats()
                }

                ChatPane(state = state, onCreateNewChat = { TODO() }) { chat ->
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, chat.id)
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { chatId ->
                    val state by viewModel.uiState.collectAsState()

                    val uiState: ChatDetailUiState? by remember(state) {
                        derivedStateOf {
                            val currentChat = state.chats.firstOrNull { it.id == chatId }
                            val otherUser = currentChat?.users?.first { user -> user != state.currentUser }
                            currentChat?.let {
                                otherUser?.let {
                                    ChatDetailUiState(currentChat, otherUser)
                                }
                            }
                        }
                    }

                    uiState?.let {
                        ChatDetailPane(it) { message ->
                            viewModel.sendMessage(
                                chatId = chatId,
                                message = message,
                                user = state.currentUser
                            )
                        }
                    }
                }
            }
        }
    )
}