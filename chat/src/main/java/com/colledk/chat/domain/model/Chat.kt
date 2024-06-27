package com.colledk.chat.domain.model

data class Chat(
    val id: String,
    val userIds: List<String>,
    val messages: List<Message>
)
