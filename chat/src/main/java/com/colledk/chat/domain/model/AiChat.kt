package com.colledk.chat.domain.model

import androidx.compose.runtime.Stable
import com.aallam.openai.api.chat.ChatMessage

@Stable
data class AiChat(
    val id: String,
    val ai: AiItem,
    val messages: List<ChatMessage>
)
