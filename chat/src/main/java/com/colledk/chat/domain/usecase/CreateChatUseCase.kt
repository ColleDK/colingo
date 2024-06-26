package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.repository.ChatRepository

class CreateChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(userIds: List<String>): Result<Chat> {
        return repository.createChat(userIds = userIds)
    }
}