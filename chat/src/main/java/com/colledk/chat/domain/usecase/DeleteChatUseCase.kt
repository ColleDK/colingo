package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.repository.ChatRepository

class DeleteChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(chatId: String): Result<Unit> {
        return repository.deleteChat(id = chatId)
    }
}