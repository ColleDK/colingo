package com.colledk.chat.domain.model

data class Chat(
    val userIds: List<String>,
    val messages: List<Message>
)
