package com.colledk.home.data.remote.model

import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.Exclude


data class ReplyRemote(
    @DocumentId
    val id: String = "",
    val userId: String = "",
    val content: String = "",
    val timestamp: Long = 0L,
    @get:Exclude
    val replies: List<ReplyRemote> = emptyList()
)