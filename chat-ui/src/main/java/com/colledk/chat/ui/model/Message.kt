package com.colledk.chat.ui.model

import java.util.Date

data class Message(
    val messageId: String,
    val message: String,
    val timestamp: Date,
    val senderId: String
)
