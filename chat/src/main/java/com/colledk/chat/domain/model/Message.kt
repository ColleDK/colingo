package com.colledk.chat.domain.model

data class Message(
    val senderId: String,
    val content: String,
    val timestamp: Long
)
