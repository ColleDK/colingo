package com.colledk.chat.ui.model

import com.colledk.profile.domain.model.Profile

data class Chat(
    val chatId: String,
    val user: Profile,
    val messages: List<Message>
)
