package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow

class GetChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String): Flow<Chat> {
        return repository.getChat(id = chatId)
    }
}