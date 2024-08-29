package com.colledk.chat.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.ui.ChatViewModel
import com.colledk.chat.ui.uistates.ChatDetailUiState
import com.colledk.chat.ui.uistates.ChatUiState
import com.colledk.common.shimmerEffect
import com.colledk.user.domain.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun ChatScreen(
    state: ChatUiState,
    error: Throwable?,
    selectChat: (ChatDetailDestination) -> Unit,
    selectedChat: ChatDetailDestination?,
    selectedPage: Int?,
    selectPage: (Int) -> Unit,
    deleteChat: (chat: Chat) -> Unit,
    sendMessage: (id: String, message: String, user: User) -> Unit,
    deleteAiChat: (chat: AiChat, userId: String) -> Unit,
    sendAiMessage: (chat: AiChat, message: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator(
        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.Detail, selectedChat).takeIf {
                selectedChat != null
            }
        )
    )

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = error) {
        if (error != null) {
            Timber.e(error)
        }
    }

    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        ListDetailPaneScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    ChatPane(
                        state = state,
                        onCreateNewChat = { /* TODO */ },
                        onChatSelected = { chat ->
                            selectChat(ChatDetailDestination.MemberChatDestination(id = chat.id))
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail, ChatDetailDestination.MemberChatDestination(id = chat.id)
                            )
                        },
                        onAiChatSelected = { chat ->
                            selectChat(ChatDetailDestination.AiChatDestination(id = chat.id))
                            navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, ChatDetailDestination.AiChatDestination(id = chat.id))
                        },
                        selectPage = selectPage,
                        selectedPage = selectedPage
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    (state as? ChatUiState.Data)?.let { state ->
                        navigator.currentDestination?.content?.let { chat ->
                            when(chat) {
                                is ChatDetailDestination.MemberChatDestination -> {
                                    val chatId = chat.id

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
                                        ChatDetailPane(
                                            uiState = it,
                                            onDeleteChat = {
                                                deleteChat(it).also {
                                                    if (navigator.canNavigateBack()) {
                                                        navigator.navigateBack()
                                                    } else {
                                                        navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null)
                                                    }
                                                }
                                            }
                                        ) { message ->
                                            sendMessage(
                                                chatId,
                                                message,
                                                state.currentUser
                                            )
                                        }
                                    }
                                }
                                is ChatDetailDestination.AiChatDestination -> {
                                    val currentChat by remember(state.aiChats) {
                                        derivedStateOf {
                                            state.aiChats.first { it.id == chat.id }
                                        }
                                    }

                                    AiChatDetailPane(
                                        chat = currentChat,
                                        onSendMessage = {
                                            sendAiMessage(
                                                currentChat,
                                                it
                                            )
                                        },
                                        onDeleteChat = {
                                            deleteAiChat(
                                                currentChat,
                                                state.currentUser.id
                                            ).also {
                                                if (navigator.canNavigateBack()) {
                                                    navigator.navigateBack()
                                                } else {
                                                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null)
                                                }
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }
}