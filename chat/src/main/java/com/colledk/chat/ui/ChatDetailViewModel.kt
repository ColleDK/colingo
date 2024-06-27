package com.colledk.chat.ui

import androidx.lifecycle.ViewModel
import com.colledk.chat.domain.usecase.GetChatUseCase
import com.colledk.chat.domain.usecase.GetChatsUseCase
import com.colledk.chat.ui.uistates.ChatDetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ChatDetailViewModel @Inject constructor(
    private val getChatsUseCase: GetChatUseCase 
) : ViewModel() {
    
    private val _uiState: MutableStateFlow<ChatDetailUiState> = MutableStateFlow(ChatDetailUiState())
    val uiState: StateFlow<ChatDetailUiState> = _uiState
    
}