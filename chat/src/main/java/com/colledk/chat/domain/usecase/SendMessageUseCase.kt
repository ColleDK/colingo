package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.domain.repository.ChatRepository

class SendMessageUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(id: String, message: Message): Result<Chat> {
        return repository.addMessage(id = id, message = message)
    }
}