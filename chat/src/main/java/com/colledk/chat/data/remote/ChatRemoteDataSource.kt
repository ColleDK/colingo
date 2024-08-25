package com.colledk.chat.data.remote

import com.colledk.chat.data.remote.model.AiChatRemote
import com.colledk.chat.data.remote.model.ChatRemote
import com.colledk.chat.data.remote.model.MessageRemote
import com.colledk.chat.domain.model.Message
import com.colledk.user.data.remote.UserRemoteDataSource.Companion.CHATS_PATH
import com.colledk.user.data.remote.UserRemoteDataSource.Companion.USERS_PATH
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import okio.IOException

class ChatRemoteDataSource(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend fun getAiChat(chatId: String): Result<AiChatRemote> {
        try {
            val chat = db
                .collection(AI_CHAT_PATH)
                .document(chatId)
                .get()
                .await()
                .toObject(AiChatRemote::class.java)

            return if (chat == null) {
                Result.failure(IOException())
            } else {
                Result.success(value = chat)
            }

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun createAiChat(aiChat: AiChatRemote): Result<AiChatRemote> {
        try {
            // Create a chat
            val chatId = db.collection(AI_CHAT_PATH).add(aiChat).await().id
            return getAiChat(chatId = chatId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateAiChat(aiChat: AiChatRemote): Result<AiChatRemote> {
        return try {
            when(val userId = auth.currentUser?.uid) {
                null -> Result.failure(IOException())
                else -> {
                    if (getAiChat(aiChat.id).getOrNull()?.userId == userId) {
                        db.collection(AI_CHAT_PATH).document(aiChat.id).set(aiChat).await()
                        getAiChat(aiChat.id)
                    } else {
                        Result.failure(IOException())
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun observeChat(chatId: String): Flow<ChatRemote> = callbackFlow {
        val document = db.collection(CHAT_PATH).document(chatId)

        val subscription = document.addSnapshotListener { value, error ->
            when(value) {
                null -> {
                    /* Handle error */
                }
                else -> {
                    value.toObject(ChatRemote::class.java)?.let {
                        trySend(it)
                    }
                }
            }
        }

        awaitClose { subscription.remove() }
    }

    suspend fun getChat(chatId: String): Result<ChatRemote> {
        try {
            val chat = db
                .collection(CHAT_PATH)
                .document(chatId)
                .get()
                .await()
                .toObject(ChatRemote::class.java)

            return if (chat == null) {
                Result.failure(IOException())
            } else {
                Result.success(value = chat)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun createChat(chat: ChatRemote): Result<ChatRemote> {
        try {
            // Create a chat
            val chatId = db.collection(CHAT_PATH).add(chat).await().id
            return getChat(chatId = chatId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateChat(chat: ChatRemote): Result<ChatRemote> {
        return try {
            when(val userId = auth.currentUser?.uid) {
                null -> Result.failure(IOException())
                else -> {
                    if (getChat(chat.id).getOrNull()?.userIds?.contains(userId) == true) {
                        db.collection(CHAT_PATH).document(chat.id).set(chat).await()
                        getChat(chat.id)
                    } else {
                        Result.failure(IOException())
                    }
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun addMessage(id: String, message: MessageRemote): Result<ChatRemote> {
        return try {
            db.collection(CHAT_PATH).document(id).update(MESSAGES_PATH, FieldValue.arrayUnion(message))
            getChat(chatId = id)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteChat(id: String): Result<Unit> {
        return try {
            db.collection(CHAT_PATH).document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAiChat(id: String): Result<Unit> {
        return try {
            db.collection(AI_CHAT_PATH).document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        const val CHAT_PATH = "chats"
        const val AI_CHAT_PATH = "ai_chats"
        const val MESSAGES_PATH = "messages"
    }
}