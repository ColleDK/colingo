package com.colledk.chat.domain.usecase

import com.aallam.openai.api.chat.ChatMessage
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.repository.ChatRepository

class UpdateAiChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(id: String, chat: AiChat): Result<AiChat> {
        return repository.updateAiChat(id = id, chat = chat)
    }
}