package com.colledk.chat.data.remote.model

import com.google.firebase.firestore.DocumentId

data class ChatRemote(
    @DocumentId
    val id: String = "",
    val userIds: List<String> = emptyList(),
    val messages: List<MessageRemote> = emptyList()
)
