package com.colledk.community.ui

import android.location.Geocoder
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.colledk.chat.domain.model.AiItem
import com.colledk.chat.domain.usecase.CreateAiChatUseCase
import com.colledk.chat.domain.usecase.CreateChatUseCase
import com.colledk.common.MessageHandler
import com.colledk.community.R
import com.colledk.user.domain.LocationHelper
import com.colledk.user.domain.model.User
import com.colledk.user.domain.pagination.UserPagingSource
import com.colledk.user.domain.usecase.AddAiChatUseCase
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val createAiChatUseCase: CreateAiChatUseCase,
    private val addAiChatUseCase: AddAiChatUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val locationHelper: LocationHelper,
    private val messageHandler: MessageHandler,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _filters: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val filters: StateFlow<List<String>> = _filters

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> = _currentUser

    val selectedUser: StateFlow<User?> = savedStateHandle.getStateFlow("selected_user", savedStateHandle["selected_user"])
    val selectedPane: StateFlow<Int?> = savedStateHandle.getStateFlow("selected_pane", savedStateHandle["selected_pane"])

    fun selectPane(pane: Int?) {
        savedStateHandle["selected_pane"] = pane
    }

    fun selectUser(user: User?) {
        savedStateHandle["selected_user"] = user
    }

    val users = Pager(
        PagingConfig(pageSize = UserPagingSource.PAGE_SIZE)
    ) {
        UserPagingSource(
            db = db,
            auth = auth,
            locationHelper = locationHelper
        ).also {
            if (_filters.value.isNotEmpty()) {
                it.updateFilter(codes = _filters.value)
            }
        }
    }.flow.cachedIn(viewModelScope)

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess {
                _currentUser.value = it
            }
        }
    }

    fun createAiChat(ai: AiItem) {
        viewModelScope.launch {
            auth.currentUser?.uid?.let { userId ->
                createAiChatUseCase(
                    userId = userId,
                    aiName = ai.name,
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.System,
                            content = ai.systemMsg
                        )
                    )
                ).onSuccess {
                    addAiChatUseCase(
                        userId = userId,
                        chatId = it.id
                    ).also {
                        messageHandler.displayMessage(R.string.chatbot_create_chat, ai.name)
                    }
                }.onFailure {
                    Timber.d(it)
                }
            }
        }
    }

    fun createChat(user: String) {
        viewModelScope.launch {
            createChatUseCase(userIds = listOfNotNull(user, auth.currentUser?.uid)).onSuccess {
                messageHandler.displayMessage(R.string.chat_created_msg)
            }.onFailure {
                messageHandler.displayError(it)
            }
        }
    }

    fun updateFilters(codes: List<String>) {
        _filters.value = codes
    }
}