package com.colledk.chat.data.remote.model

data class MessageRemote(
    val id: String,
    val senderId: String,
    val content: String,
    val timestamp: Long
)
