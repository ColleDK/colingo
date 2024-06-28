package com.colledk.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.chat.domain.usecase.GetChatUseCase
import com.colledk.chat.domain.usecase.GetChatsUseCase
import com.colledk.chat.ui.uistates.ChatDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val getChatUseCase: GetChatUseCase
) : ViewModel() {
    
    private val _uiState: MutableStateFlow<ChatDetailUiState> = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState

    fun getChat(id: String) {
        viewModelScope.launch {
            getChatUseCase(chatId = id).collectLatest {
                _uiState.value = ChatDetailUiState(chat = it)
            }
        }
    }
}