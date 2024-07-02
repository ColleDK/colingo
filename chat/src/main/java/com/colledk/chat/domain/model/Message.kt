package com.colledk.chat.domain.model

import com.colledk.user.domain.model.User

data class Message(
    val id: String,
    val sender: User,
    val content: String,
    val time: String,
    val date: String,
    val timestamp: Long
)
