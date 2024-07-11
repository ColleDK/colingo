package com.colledk.chat.data.remote.model

data class AiChatRemote(
    val id: String = "",
    val aiName: String = "",
    val userId: String = "",
    val messages: List<AiMessageRemote> = emptyList()
)