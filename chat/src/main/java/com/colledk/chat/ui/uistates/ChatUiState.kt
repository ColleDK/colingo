package com.colledk.chat.ui.uistates

import androidx.compose.runtime.Stable
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.model.Chat
import com.colledk.user.domain.model.User

@Stable
data class ChatUiState(
    val chats: List<Chat> = emptyList(),
    val currentUser: User = User(),
    val aiChats: List<AiChat> = emptyList()
)
