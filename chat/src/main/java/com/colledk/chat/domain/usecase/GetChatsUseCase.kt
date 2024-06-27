package com.colledk.chat.domain.usecase

import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.repository.ChatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import okio.IOException

class GetChatsUseCase(private val repository: ChatRepository) {
    suspend operator fun invoke(ids: List<String>): Flow<List<Chat>> {
        return combine(
            ids.map {
                repository.getChat(it)
            }
        ) {
            it.toList()
        }
    }
}