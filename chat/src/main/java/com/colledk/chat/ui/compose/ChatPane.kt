package com.colledk.chat.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.ui.uistates.ChatUiState
import com.colledk.user.domain.model.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ChatPane(
    state: ChatUiState,
    modifier: Modifier = Modifier,
    onChatSelected: (chat: Chat) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        stickyHeader {
            Text(
                text = "My messages",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        items(state.chats) { chat ->
            ChatItem(
                chat = chat,
                onChatSelected = { onChatSelected(chat) },
                currentUser = state.currentUser
            )
        }
    }
}

@Composable
private fun ChatItem(
    currentUser: User,
    chat: Chat,
    modifier: Modifier = Modifier,
    onChatSelected: () -> Unit
) {
    Card(
        onClick = onChatSelected,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        val latestMessage: Message? by remember(chat.messages) {
            derivedStateOf {
                chat.messages.lastOrNull()
            }
        }

        val latestSender: User? by remember(chat.messages) {
            derivedStateOf {
                val lastMessage = chat.messages.lastOrNull()
                chat.users.find { it.id == lastMessage?.id }
            }
        }

        val otherUser: User by remember {
            derivedStateOf {
                chat.users.first { it.id != currentUser.id }
            }
        }

        Row {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                latestSender?.profilePictures?.firstOrNull().let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = otherUser.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = otherUser.name,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
            Column {
                Text(text = latestMessage?.time.orEmpty())
                // TODO add unread notification
            }
        }
    }
}