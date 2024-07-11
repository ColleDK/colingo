package com.colledk.user.domain.usecase

import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository

class AddAiChatUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String, chatId: String): Result<User> {
        return repository.addAiChat(userId = userId, chatId = chatId)
    }
}