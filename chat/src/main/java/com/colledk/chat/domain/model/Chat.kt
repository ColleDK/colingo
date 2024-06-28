package com.colledk.chat.domain.model

import com.colledk.user.domain.model.User

data class Chat(
    val id: String,
    val users: List<User>,
    val messages: List<Message>
)
