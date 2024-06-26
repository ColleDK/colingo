package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.repository.ChatRepository
import okio.IOException

class GetChatsUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(): Result<List<Chat>> {
        return Result.failure(IOException())
    }
}