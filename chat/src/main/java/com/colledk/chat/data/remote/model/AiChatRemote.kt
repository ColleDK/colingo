package com.colledk.chat.data.remote.model

import com.google.firebase.firestore.DocumentId

data class AiChatRemote(
    @DocumentId
    val id: String = "",
    val aiName: String = "",
    val userId: String = "",
    val messages: List<AiMessageRemote> = emptyList()
)