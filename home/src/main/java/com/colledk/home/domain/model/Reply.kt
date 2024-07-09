package com.colledk.home.domain.model

import com.colledk.user.domain.model.User
import org.joda.time.DateTime

data class Reply(
    val id: String,
    val user: User,
    val content: String,
    val timestamp: DateTime,
    val replies: List<Reply>
)
