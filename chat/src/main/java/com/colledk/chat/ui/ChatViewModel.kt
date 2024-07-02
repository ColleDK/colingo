package com.colledk.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.chat.domain.model.Message
import com.colledk.chat.domain.usecase.GetChatsUseCase
import com.colledk.chat.domain.usecase.UpdateChatUseCase
import com.colledk.chat.ui.uistates.ChatUiState
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
    private val updateChatUseCase: UpdateChatUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<ChatUiState> = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    fun getChats() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { user ->
                getChatsUseCase(user.chats).collectLatest {
                    _uiState.value = ChatUiState(
                        chats = it,
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
            updateChatUseCase(
                id = chatId,
                newMessage = Message(
                    id = UUID.randomUUID().toString(),
                    sender = user,
                    content = message,
                    timestamp = DateTime.now().millis,
                    date = "",
                    time = ""
                )
            )
        }
    }
}