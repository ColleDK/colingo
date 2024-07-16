package com.colledk.chat.ui.compose

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aallam.openai.api.core.Role
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.model.Message
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.User

@Composable
internal fun AiChatPane(
    aiChats: List<AiChat>,
    onChatClicked: (AiChat) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(aiChats) { chat ->
            AiChatItem(
                chat = chat,
                onChatSelected = { onChatClicked(chat) },
                modifier = Modifier.fillParentMaxWidth()
            )
        }
    }
}

@Composable
private fun AiChatItem(
    chat: AiChat,
    onChatSelected: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lastMessage by remember(chat.messages) {
        derivedStateOf {
            chat.messages.lastOrNull { it.role != Role.System }
        }
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier.heightIn(min = 140.dp),
        onClick = onChatSelected
    ) {
        val name by remember(chat.ai) {
            mutableStateOf(chat.ai.name.lowercase().capitalize(Locale.current))
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ProfilePicture(profilePicture = chat.ai.image)
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = lastMessage?.content.orEmpty(),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfilePicture(
    @DrawableRes profilePicture: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
    ) {
        Image(
            painter = painterResource(id = profilePicture),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}