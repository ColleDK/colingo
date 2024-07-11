package com.colledk.chat.domain.model

import com.aallam.openai.api.chat.ChatMessage

data class AiChat(
    val id: String,
    val aiName: String,
    val messages: List<ChatMessage>
)
