package com.colledk.community.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
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

    fun createAiChat() {
        viewModelScope.launch {
            auth.uid?.let { userId ->
                createAiChatUseCase(
                    userId = userId,
                    aiName = "Louise",
                    messages = listOf(
                        ChatMessage(
                            role = ChatRole.System,
                            content = "You are a spanish teacher, named Louise, trying to teach spanish to a student. You may only answer in spanish." +
                                    "You will correct any mistakes by the student, but give examples about how to correct it for the next time!"
                        )
                    )
                ).onSuccess {
                    Timber.d("Created ai chat $it")
                    addAiChatUseCase(
                        userId = userId,
                        chatId = it.id
                    )
                }.onFailure {
                    Timber.d("Failed to create ai chat $it")
                }
            }
        }
    }
}