package com.colledk.chat.domain.repository

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    suspend fun getChats(ids: List<String>): Flow<List<Chat>>
    suspend fun getChat(id: String): Flow<Chat>
    suspend fun createChat(userIds: List<String>): Result<Chat>
    suspend fun updateChat(id: String, message: Message): Result<Chat>
    suspend fun deleteChat(id: String): Result<Unit>
}