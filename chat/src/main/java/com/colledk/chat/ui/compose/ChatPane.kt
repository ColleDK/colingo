package com.colledk.chat.ui.compose

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.colledk.chat.R
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.ui.uistates.ChatUiState
import com.colledk.theme.ColingoTheme
import com.colledk.theme.PreviewAnnotations
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.Location
import com.colledk.user.domain.model.User

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ChatPane(
    state: ChatUiState,
    onCreateNewChat: () -> Unit,
    modifier: Modifier = Modifier,
    onChatSelected: (chat: Chat) -> Unit
) {
    val listState = rememberLazyListState()

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                text = {
                    Text(
                        text = stringResource(id = R.string.chat_create_new),
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = R.drawable.pen),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                },
                onClick = onCreateNewChat,
                expanded = listState.isScrollingUp()
            )
        }
    ) {
        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .padding(it),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            state = listState
        ) {
            stickyHeader {
                Text(
                    text = "My messages",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.fillParentMaxWidth().background(MaterialTheme.colorScheme.surface)
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
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) {
        mutableIntStateOf(firstVisibleItemIndex)
    }

    var previousScrollOffset by remember(this) {
        mutableIntStateOf(firstVisibleItemScrollOffset)
    }

    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
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
        modifier = modifier.height(140.dp),
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

        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
                        contentScale = ContentScale.Crop,
                        placeholder = debugPlaceholder()
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = otherUser.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = latestMessage?.content.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Column {
                Text(text = latestMessage?.time.orEmpty())
                // TODO add unread notification
            }
        }
    }
}

@PreviewAnnotations
@Composable
private fun ChatPanePreview() {
    val uiState by remember {
        mutableStateOf(
            ChatUiState(
                chats = listOf(
                    Chat(
                        id = "123",
                        users = listOf(
                            User(
                                id = "12",
                                name = "Johnny",
                                birthday = 0L,
                                profilePictures = emptyList(),
                                description = "",
                                location = Location("", ""),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                friends = emptyList(),
                                chats = listOf("123")
                            ),
                            User(
                                id = "13",
                                name = "Benny",
                                birthday = 0L,
                                profilePictures = listOf(Uri.parse("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg")),
                                description = "",
                                location = Location("", ""),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                friends = emptyList(),
                                chats = listOf("123")
                            )
                        ),
                        messages = listOf(
                            Message(
                                id = "123",
                                sender = User(
                                    id = "13",
                                    name = "Benny",
                                    birthday = 0L,
                                    profilePictures = emptyList(),
                                    description = "",
                                    location = Location("", ""),
                                    languages = emptyList(),
                                    gender = Gender.MALE,
                                    friends = emptyList(),
                                    chats = listOf("123")
                                ),
                                content = "Hi Johnny, I really love you big time my man",
                                time = "12:53",
                                timestamp = 0L
                            )
                        )
                    ),
                    Chat(
                        id = "123",
                        users = listOf(
                            User(
                                id = "12",
                                name = "Johnny",
                                birthday = 0L,
                                profilePictures = emptyList(),
                                description = "",
                                location = Location("", ""),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                friends = emptyList(),
                                chats = listOf("123")
                            ),
                            User(
                                id = "13",
                                name = "Benny",
                                birthday = 0L,
                                profilePictures = listOf(Uri.parse("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg")),
                                description = "",
                                location = Location("", ""),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                friends = emptyList(),
                                chats = listOf("123")
                            )
                        ),
                        messages = listOf(
                            Message(
                                id = "123",
                                sender = User(
                                    id = "13",
                                    name = "Benny",
                                    birthday = 0L,
                                    profilePictures = emptyList(),
                                    description = "",
                                    location = Location("", ""),
                                    languages = emptyList(),
                                    gender = Gender.MALE,
                                    friends = emptyList(),
                                    chats = listOf("123")
                                ),
                                content = "Hi Johnny, I really love you big time my man",
                                time = "12:53",
                                timestamp = 0L
                            )
                        )
                    )
                )
            )
        )
    }

    ColingoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ChatPane(state = uiState, onCreateNewChat = {}) {

            }
        }
    }
}