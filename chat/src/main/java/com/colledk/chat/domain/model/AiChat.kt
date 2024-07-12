package com.colledk.chat.domain.model

import com.aallam.openai.api.chat.ChatMessage

data class AiChat(
    val id: String,
    val ai: AiItem,
    val messages: List<ChatMessage>
)
