package com.colledk.chat.domain.usecase

import com.aallam.openai.api.chat.ChatMessage
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.repository.ChatRepository

class CreateAiChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(userId: String, aiName: String, messages: List<ChatMessage>): Result<AiChat> {
        return repository.createAiChat(userId = userId, aiName = aiName, messages = messages)
    }
}