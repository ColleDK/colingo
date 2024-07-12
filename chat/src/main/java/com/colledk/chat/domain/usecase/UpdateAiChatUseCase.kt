package com.colledk.chat.domain.usecase

import com.aallam.openai.api.chat.ChatMessage
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.repository.ChatRepository

class UpdateAiChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(id: String, message: ChatMessage): Result<AiChat> {
        return repository.updateAiChat(id = id, message = message)
    }
}