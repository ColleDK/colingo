package com.colledk.chat.ui.uistates

import androidx.compose.runtime.Stable
import com.colledk.chat.domain.model.Chat
import com.colledk.user.domain.model.User

@Stable
data class ChatDetailUiState(
    val chat: Chat,
    val otherUser: User
)
