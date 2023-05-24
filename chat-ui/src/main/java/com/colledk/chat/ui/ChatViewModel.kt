package com.colledk.chat.ui

import androidx.lifecycle.ViewModel
import com.colledk.chat.ui.model.Chat
import com.colledk.profile.domain.model.Profile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(

) : ViewModel() {

    private val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chat: StateFlow<List<Chat>> = _chats

    init {
        _chats.value = listOf(
            Chat(
                chatId = "1",
                user = Profile(
                    name = "John",
                    profilePicUrl = "",
                    description = null,
                    languagesKnown = listOf(),
                    languagesLearning = listOf(),
                    birthday = Date()
                ),
                messages = listOf()
            ),
            Chat(
                chatId = "2",
                user = Profile(
                    name = "Karl",
                    profilePicUrl = "",
                    description = null,
                    languagesKnown = listOf(),
                    languagesLearning = listOf(),
                    birthday = Date()
                ),
                messages = listOf()
            ),
            Chat(
                chatId = "3",
                user = Profile(
                    name = "Test",
                    profilePicUrl = "",
                    description = null,
                    languagesKnown = listOf(),
                    languagesLearning = listOf(),
                    birthday = Date()
                ),
                messages = listOf()
            ),
            Chat(
                chatId = "4",
                user = Profile(
                    name = "Julia",
                    profilePicUrl = "",
                    description = null,
                    languagesKnown = listOf(),
                    languagesLearning = listOf(),
                    birthday = Date()
                ),
                messages = listOf()
            )
        )
    }
}