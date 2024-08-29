package com.colledk.chat.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.colledk.chat.R
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.domain.usecase.DeleteAiChatUseCase
import com.colledk.chat.domain.usecase.DeleteChatUseCase
import com.colledk.chat.domain.usecase.GetAiChatsUseCase
import com.colledk.chat.domain.usecase.GetChatsUseCase
import com.colledk.chat.domain.usecase.SendMessageUseCase
import com.colledk.chat.domain.usecase.UpdateAiChatUseCase
import com.colledk.chat.ui.compose.ChatDetailDestination
import com.colledk.chat.ui.uistates.ChatUiState
import com.colledk.common.MessageHandler
import com.colledk.user.domain.model.User
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.Date
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val getChatsUseCase: GetChatsUseCase,
    private val sendMessageUseCase: SendMessageUseCase,
    private val getAiChatsUseCase: GetAiChatsUseCase,
    private val updateAiChatUseCase: UpdateAiChatUseCase,
    private val openAI: OpenAI,
    private val deleteChatUseCase: DeleteChatUseCase,
    private val deleteAiChatUseCase: DeleteAiChatUseCase,
    private val messageHandler: MessageHandler,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState.Loading)
    val uiState: StateFlow<ChatUiState> = _uiState

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    val selectedPane: StateFlow<Int?> = savedStateHandle.getStateFlow("selected_pane", savedStateHandle["selected_pane"])
    val selectedChat: StateFlow<ChatDetailDestination?> = savedStateHandle.getStateFlow("chat_details", savedStateHandle["chat_details"])

    fun selectChat(destination: ChatDetailDestination?) {
        savedStateHandle["chat_details"] = destination
    }

    fun selectPane(page: Int?) {
        savedStateHandle["selected_pane"] = page
    }

    fun getChats() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { user ->
                if (user.chats.isEmpty()) {
                    emitUiState(
                        chats = emptyList(),
                        currentUser = user
                    )
                } else {
                    getChatsUseCase(user.chats).collectLatest {
                        emitUiState(
                            chats = it,
                            currentUser = user
                        )
                    }
                }
            }.onFailure {
                _error.trySend(it)
            }
        }
    }

    fun getAiChats() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { user ->
                getAiChatsUseCase(user.aiChats).onSuccess {
                    emitUiState(
                        aiChats = it,
                        currentUser = user
                    )
                }
            }.onFailure {
                _error.trySend(it)
            }
        }
    }

    fun sendMessage(chatId: String, message: String, user: User) {
        viewModelScope.launch {
            sendMessageUseCase(
                id = chatId,
                message = Message(
                    id = UUID.randomUUID().toString(),
                    sender = user,
                    content = message,
                    timestamp = DateTime.now().millis,
                    date = "",
                    time = ""
                )
            ).onFailure {
                _error.trySend(it)
            }
        }
    }

    fun sendAiMessage(chat: AiChat, message: String) {
        viewModelScope.launch {
            val chatCompletionRequest = ChatCompletionRequest(
                model = ModelId("gpt-3.5-turbo"),
                messages = listOf(
                    *chat.messages.toTypedArray(),
                    ChatMessage.Companion.User(content = message)
                )
            )

            val response = openAI.chatCompletion(chatCompletionRequest)
            val newChat = chat.copy(
                messages = listOfNotNull(
                    *chat.messages.toTypedArray(),
                    ChatMessage.Companion.User(content = message),
                    response.choices.firstOrNull()?.message
                )
            )

            updateAiChatUseCase(
                id = chat.id,
                chat = newChat
            ).onFailure {
                _error.trySend(it)
            }.onSuccess {
                getAiChats()
            }
        }
    }

    fun deleteChat(chat: Chat) {
        viewModelScope.launch {
            deleteChatUseCase(chatId = chat.id, userIds = chat.users.map { it.id }).onSuccess {
                getChats().also {
                    messageHandler.displayMessage(R.string.chats_delete_success)
                    selectChat(null)
                }
            }.onFailure {
                _error.trySend(it)
            }
        }
    }

    fun deleteAiChat(chat: AiChat, userId: String) {
        viewModelScope.launch {
            deleteAiChatUseCase(chat.id, userId).onSuccess {
                getAiChats().also {
                    messageHandler.displayMessage(R.string.chats_delete_success)
                    selectChat(null)
                }
            }.onFailure {
                _error.trySend(it)
            }
        }
    }

    private fun emitUiState(
        chats: List<Chat>? = null,
        currentUser: User? = null,
        aiChats: List<AiChat>? = null
    ) {
        val currentState = _uiState.value as? ChatUiState.Data

        _uiState.value = ChatUiState.Data(
            chats = chats ?: currentState?.chats.orEmpty(),
            currentUser = currentUser ?: currentState?.currentUser ?: User(),
            aiChats = aiChats ?: currentState?.aiChats.orEmpty()
        )
    }
}