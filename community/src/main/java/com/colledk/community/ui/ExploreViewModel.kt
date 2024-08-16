package com.colledk.community.ui

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
import com.colledk.user.domain.model.User
import com.colledk.user.domain.pagination.UserPagingSource
import com.colledk.user.domain.usecase.AddAiChatUseCase
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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
    private val createChatUseCase: CreateChatUseCase
) : ViewModel() {
    private val _filters: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val filters: StateFlow<List<String>> = _filters

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> = _currentUser

    val users = Pager(
        PagingConfig(pageSize = UserPagingSource.PAGE_SIZE)
    ) {
        UserPagingSource(
            db = db,
            auth = auth
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
                    )
                }.onFailure {
                    Timber.d(it)
                }
            }
        }
    }

    fun createChat(user: String) {
        viewModelScope.launch {
            createChatUseCase(userIds = listOfNotNull(user, auth.currentUser?.uid)).onSuccess {
                Timber.d("Created chat ${it.id}")
            }.onFailure {
                Timber.e("Failed to create chat $it")
            }
        }
    }

    fun updateFilters(codes: List<String>) {
        _filters.value = codes
    }
}