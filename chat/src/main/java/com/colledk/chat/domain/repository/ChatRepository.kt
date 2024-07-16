package com.colledk.chat.domain.repository

import com.aallam.openai.api.chat.ChatMessage
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    // Ai chats
    suspend fun getAiChats(ids: List<String>): Result<List<AiChat>>
    suspend fun getAiChat(id: String): Result<AiChat>
    suspend fun createAiChat(userId: String, aiName: String, messages: List<ChatMessage>): Result<AiChat>
    suspend fun updateAiChat(id: String, chat: AiChat): Result<AiChat>
    suspend fun deleteAiChat(id: String): Result<Unit>

    // Normal chats
    suspend fun getChats(ids: List<String>): Flow<List<Chat>>
    suspend fun getChat(id: String): Flow<Chat>
    suspend fun createChat(userIds: List<String>): Result<Chat>
    suspend fun updateChat(id: String, message: Message): Result<Chat>
    suspend fun deleteChat(id: String): Result<Unit>
}