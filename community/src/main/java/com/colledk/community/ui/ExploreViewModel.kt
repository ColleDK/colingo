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
import com.colledk.user.domain.pagination.UserPagingSource
import com.colledk.user.domain.usecase.AddAiChatUseCase
import com.colledk.user.domain.usecase.UpdateUserUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val userPagingSource: UserPagingSource,
    private val createAiChatUseCase: CreateAiChatUseCase,
    private val addAiChatUseCase: AddAiChatUseCase,
    private val auth: FirebaseAuth
) : ViewModel() {
    val users = Pager(
        PagingConfig(pageSize = UserPagingSource.PAGE_SIZE)
    ) {
        userPagingSource
    }.flow.cachedIn(viewModelScope)

    fun createAiChat(ai: AiItem) {
        viewModelScope.launch {
            auth.uid?.let { userId ->
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
                    // TODO handle error
                }
            }
        }
    }
}