package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.repository.ChatRepository

class DeleteAiChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String, userId: String): Result<Unit> {
        return repository.deleteAiChat(id = chatId, userId = userId)
    }
}