package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.repository.ChatRepository

class GetAiChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(id: String): Result<AiChat> {
        return repository.getAiChat(id = id)
    }
}