package com.colledk.chat.ui.uistates

import androidx.compose.runtime.Stable
import com.colledk.chat.domain.model.Chat

@Stable
data class ChatUiState(
    val chats: List<Chat>
)
