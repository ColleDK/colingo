package com.colledk.chat.data.remote.repository

import com.colledk.chat.data.mapToDomain
import com.colledk.chat.data.mapToRemote
import com.colledk.chat.data.remote.ChatRemoteDataSource
import com.colledk.chat.data.remote.model.ChatRemote
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.domain.repository.ChatRepository
import com.colledk.user.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okio.IOException

class ChatRepositoryImpl(
    private val remoteDataSource: ChatRemoteDataSource,
    private val userRepository: UserRepository,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : ChatRepository {
    override suspend fun getChats(ids: List<String>): Flow<List<Chat>> = withContext(dispatcher) {
        combine(
            ids.map { getChat(it) }
        ) {
            it.toList()
        }
    }

    override suspend fun getChat(id: String): Flow<Chat> = withContext(dispatcher) {
        remoteDataSource.observeChat(id).map {
            val users = it.userIds.mapNotNull { userRepository.getUser(it).getOrNull() }
            it.mapToDomain(users = users)
        }
    }

    override suspend fun createChat(userIds: List<String>): Result<Chat> = withContext(dispatcher) {
        val users = userIds.mapNotNull { userRepository.getUser(it).getOrNull() }
        // If we didn't retrieve all users, we cannot add the chat to their account, so we fail the call
        if (users.size != userIds.size) {
            Result.failure(IOException())
        } else {
            remoteDataSource.createChat(
                ChatRemote(
                    id = "", // Empty id, will be updated after creating the chat
                    userIds = userIds,
                    messages = emptyList()
                )
            ).map {
                it.mapToDomain(users)
            }
        }
    }

    override suspend fun updateChat(id: String, message: Message): Result<Chat> = withContext(dispatcher) {
        val chat = remoteDataSource.getChat(id).getOrThrow()
        val updatedChat = chat.copy(messages = listOf(*chat.messages.toTypedArray(), message.mapToRemote()))
        remoteDataSource.updateChat(updatedChat).map {
            val users = it.userIds.mapNotNull { userRepository.getUser(it).getOrNull() }
            it.mapToDomain(users = users)
        }
    }

    override suspend fun deleteChat(id: String): Result<Unit> {
        TODO("Not yet implemented")
    }
}