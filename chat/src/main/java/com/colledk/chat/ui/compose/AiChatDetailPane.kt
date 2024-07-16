package com.colledk.chat.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.core.Role
import com.colledk.chat.R
import com.colledk.chat.domain.model.AiChat
import kotlinx.coroutines.launch
import timber.log.Timber

@Composable
internal fun AiChatDetailPane(
    chat: AiChat,
    modifier: Modifier = Modifier,
    onSendMessage: (message: String) -> Unit
) {
    val userMessages by remember(chat.messages) {
        derivedStateOf {
            chat.messages.filter { it.role != Role.System }
        }
    }

    val allMessages = remember(userMessages) {
        mutableStateListOf(*userMessages.toTypedArray())
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ChatTopBar(title = chat.ai.name.lowercase().capitalize(Locale.current))
        },
        bottomBar = {
            ChatBottomBar {
                allMessages.add(ChatMessage.User(it))
                onSendMessage(it)
            }
        }
    ) {
        val scope = rememberCoroutineScope()
        val listState = rememberLazyListState()

        LaunchedEffect(key1 = allMessages.size) {
            scope.launch {
                listState.animateScrollToItem(allMessages.indices.last)
            }
        }

        LazyColumn(
            modifier = Modifier.padding(it),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            state = listState
        ) {
            items(allMessages) { message ->
                ChatMessageItem(
                    message = message,
                    aiImage = chat.ai.image,
                    isCurrentUser = message.role == Role.User
                )
            }
            item { Spacer(modifier = Modifier.height(12.dp)) }
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

@Composable
private fun ChatMessageItem(
    isCurrentUser: Boolean,
    message: ChatMessage,
    @DrawableRes aiImage: Int,
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
                    Image(
                        painter = painterResource(id = aiImage),
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                }
            }
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isCurrentUser) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.tertiaryContainer
                ),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = message.content.orEmpty(),
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isCurrentUser) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onTertiaryContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
    }
}