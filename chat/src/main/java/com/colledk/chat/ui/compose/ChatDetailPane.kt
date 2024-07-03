package com.colledk.chat.ui.compose

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.colledk.chat.R
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.ui.uistates.ChatDetailUiState
import com.colledk.theme.ColingoTheme
import com.colledk.theme.PreviewAnnotations
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.Location
import com.colledk.user.domain.model.User
import org.joda.time.DateTime

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun ChatDetailPane(
    uiState: ChatDetailUiState,
    modifier: Modifier = Modifier,
    onSendMessage: (message: String) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            ChatTopBar(title = uiState.otherUser.name)
        },
        bottomBar = {
            ChatBottomBar {
                onSendMessage(it)
            }
        }
    ) {
        val days by remember(uiState.chat.messages) {
            derivedStateOf {
                uiState.chat.messages
                    .sortedBy { message -> message.timestamp }
                    .groupBy { message -> message.date }
            }
        }

        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            days.forEach { (day, messages) ->
                stickyHeader {
                    Surface(modifier = Modifier.fillParentMaxWidth()) {
                        Text(
                            text = day,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                items(messages) { message ->
                    ChatMessageItem(
                        message = message,
                        isCurrentUser = message.sender.id != uiState.otherUser.id
                    )
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }
        }
    }
}

@Composable
private fun ChatBottomBar(
    modifier: Modifier = Modifier,
    onSendMessage: (message: String) -> Unit
) {
    BottomAppBar(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
    ) {
        var messageText by remember {
            mutableStateOf("")
        }

        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier.weight(1f)
        )
        Spacer(modifier = Modifier.width(12.dp))
        IconButton(onClick = { onSendMessage(messageText).also { messageText = "" } }) {
            Icon(
                painter = painterResource(id = R.drawable.send),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(
    title: String,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        actions = {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.menu_dots),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}

@Composable
private fun ChatMessageItem(
    isCurrentUser: Boolean,
    message: Message,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth(.8f)
                .align(if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            if (!isCurrentUser) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface)
                ) {
                    message.sender.profilePictures.firstOrNull()?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            placeholder = debugPlaceholder()
                        )
                    }
                }
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrentUser) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = message.content,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}

@PreviewAnnotations
@Composable
private fun ChatDetailPanePreview() {
    val uiState: ChatDetailUiState by remember {
        mutableStateOf(
            ChatDetailUiState(
                chat = Chat(
                    id = "123",
                    users = listOf(
                        User(
                            id = "12",
                            name = "Johnny",
                            birthday = DateTime.now(),
                            profilePictures = emptyList(),
                            description = "",
                            location = Location("", ""),
                            languages = emptyList(),
                            gender = Gender.MALE,
                            chats = listOf("123")
                        ),
                        User(
                            id = "13",
                            name = "Benny",
                            birthday = DateTime.now(),
                            profilePictures = listOf(Uri.parse("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg")),
                            description = "",
                            location = Location("", ""),
                            languages = emptyList(),
                            gender = Gender.MALE,
                            chats = listOf("123")
                        )
                    ),
                    messages = listOf(
                        Message(
                            id = "123",
                            sender = User(
                                id = "13",
                                name = "Benny",
                                birthday = DateTime.now(),
                                profilePictures = listOf(Uri.parse("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg")),
                                description = "",
                                location = Location("", ""),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                chats = listOf("123")
                            ),
                            content = "Hi Johnny, I really love you big time my man",
                            time = "12:53",
                            timestamp = 0L,
                            date = "Yesterday"
                        ),
                        Message(
                            id = "123",
                            sender = User(
                                id = "12",
                                name = "Johnny",
                                birthday = DateTime.now(),
                                profilePictures = listOf(Uri.parse("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg")),
                                description = "",
                                location = Location("", ""),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                chats = listOf("123")
                            ),
                            content = "Hi Benny, I really love you big time my man",
                            time = "12:53",
                            timestamp = 0L,
                            date = "Today"
                        )
                    )
                ),
                otherUser = User(
                    id = "12",
                    name = "Johnny",
                    birthday = DateTime.now(),
                    profilePictures = emptyList(),
                    description = "",
                    location = Location("", ""),
                    languages = emptyList(),
                    gender = Gender.MALE,
                    chats = listOf("123")
                )
            )
        )
    }

    ColingoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ChatDetailPane(uiState = uiState) {

            }
        }
    }
}