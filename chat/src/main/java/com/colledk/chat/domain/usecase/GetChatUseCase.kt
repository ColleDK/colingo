package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.repository.ChatRepository

class GetChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String): Result<Chat> {
        return repository.getChat(id = chatId)
    }
}