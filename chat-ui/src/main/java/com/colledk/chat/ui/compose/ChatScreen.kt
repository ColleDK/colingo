package com.colledk.chat.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.colledk.chat.ui.ChatViewModel
import com.colledk.chat.ui.model.Chat
import com.colledk.profile.domain.model.Profile
import java.util.Date

@Composable
internal fun ChatScreen(
    navController: NavHostController,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val chats by viewModel.chat.collectAsState()

    ChatScreen(chats = chats)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    chats: List<Chat>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        stickyHeader {
            ChatScreenTitle()
        }
        items(chats) { chat ->
            ChatListItem(
                chat = chat,
                navigateToProfile = {}
            )
        }
    }
}

@Composable
private fun ChatScreenTitle() {
    Text(
        text = "Chats",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center
    )

    Divider()
}


@Preview(
    name = "Extra large font",
    group = "Font group",
    fontScale = 2.5f,
    showBackground = true
)
@Preview(
    name = "Large font",
    group = "Font group",
    fontScale = 1.5f,
    showBackground = true
)
@Preview(
    name = "Normal font",
    group = "Font group",
    fontScale = 1f,
    showBackground = true
)
@Composable
fun ChatScreenPreview() {
    ChatScreen(
        chats = listOf(
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
    )
}