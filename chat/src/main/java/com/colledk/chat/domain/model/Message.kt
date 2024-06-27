package com.colledk.chat.domain.model

data class Message(
    val id: String,
    val senderId: String,
    val content: String,
    val timestamp: Long
)
