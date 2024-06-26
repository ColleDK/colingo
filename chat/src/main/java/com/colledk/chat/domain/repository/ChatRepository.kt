package com.colledk.chat.domain.repository

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun createChat(userIds: List<String>): Result<Chat>
    suspend fun getChat(id: String): Result<Chat>
    suspend fun observeChat(id: String): Result<Flow<Chat>>
    suspend fun updateChat(message: Message): Result<Chat>
    suspend fun deleteChat(id: String): Result<Unit>
}