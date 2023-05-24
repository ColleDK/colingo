package com.colledk.chat

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.colledk.accessibility.BaseAccessibilityTest
import com.colledk.chat.ui.compose.ChatScreen
import com.colledk.chat.ui.model.Chat
import com.colledk.profile.domain.model.Profile
import org.junit.Test
import java.util.Date

class ChatScreenUiTest: BaseAccessibilityTest() {

    @Test
    fun signUpAccessibilityTest() {
        composeTestRule.setContent {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                ChatScreen(
                    chats = TEST_CHATS
                )
            }
        }

        testAccessibility()
    }

    companion object {
        val TEST_CHATS = listOf(
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