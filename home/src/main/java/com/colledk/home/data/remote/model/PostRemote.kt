package com.colledk.home.data.remote.model

import com.colledk.user.data.remote.model.TopicRemote
import com.google.firebase.firestore.Exclude

data class PostRemote(
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    @get:Exclude
    val replies: List<ReplyRemote> = emptyList(),
    val likes: List<String> = emptyList(),
    val timestamp: Long = 0L,
    val topics: List<TopicRemote> = emptyList()
)
