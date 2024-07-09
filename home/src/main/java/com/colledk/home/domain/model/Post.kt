package com.colledk.home.domain.model

import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.User
import org.joda.time.DateTime

data class Post(
    val id: String,
    val user: User,
    val content: String,
    val replies: List<Reply>,
    val likes: Int,
    val timestamp: DateTime,
    val topics: List<Topic>
)
