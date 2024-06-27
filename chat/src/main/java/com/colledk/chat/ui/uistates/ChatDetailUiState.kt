package com.colledk.chat.ui.uistates

import androidx.compose.runtime.Stable
import com.colledk.chat.domain.model.Chat

@Stable
data class ChatDetailUiState(
    val chat: Chat? = null
)
