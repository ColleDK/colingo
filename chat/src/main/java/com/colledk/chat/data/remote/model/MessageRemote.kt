package com.colledk.chat.data.remote.model

import com.google.firebase.firestore.DocumentId

data class MessageRemote(
    @DocumentId
    val id: String = "",
    val senderId: String = "",
    val content: String = "",
    val timestamp: Long = 0L
)
