package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.repository.ChatRepository

class GetAiChatsUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(ids: List<String>): Result<List<AiChat>> {
        return repository.getAiChats(ids = ids)
    }
}