package com.colledk.home.data.remote.model


data class ReplyRemote(
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    val replies: List<ReplyRemote> = emptyList()
)