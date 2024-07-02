package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.domain.repository.ChatRepository

class UpdateChatUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(id: String, newMessage: Message): Result<Chat> {
        return repository.updateChat(id = id, message = newMessage)
    }
}