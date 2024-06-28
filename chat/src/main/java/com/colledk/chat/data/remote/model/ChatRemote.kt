package com.colledk.chat.data.remote.model

data class ChatRemote(
    val id: String,
    val userIds: List<String>,
    val messages: List<MessageRemote>
)
