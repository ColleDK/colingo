package com.colledk.community.ui.compose

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.colledk.chat.domain.model.AiItem
import com.colledk.community.R

@Composable
internal fun ChatBotsPane(
    currentFilters: List<String>,
    onCreateAiChat: (ai: AiItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val ais by remember(currentFilters) {
        derivedStateOf {
            if (currentFilters.isNotEmpty()) {
                AiItem.entries.filter { currentFilters.contains(it.code) }
            } else {
                AiItem.entries
            }

        }
    }

    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(ais.size) { index ->
            BotItem(
                ai = ais[index],
                onCreateChat = { onCreateAiChat(ais[index]) },
                modifier = Modifier.fillParentMaxWidth()
            )
        }
        item {
            if (ais.isEmpty()) {
                EmptyScreen(modifier = Modifier.fillParentMaxSize())
            }
        }
    }
}

@Composable
private fun EmptyScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.community_ai_empty_title), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Text(text = stringResource(id = R.string.community_ai_empty_subtitle), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    }
}

@Composable
private fun BotItem(
    ai: AiItem,
    onCreateChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier.heightIn(min = 140.dp)
    ) {
        val name by remember(ai) {
            mutableStateOf(ai.name.lowercase().capitalize(Locale.current))
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
                ProfilePicture(profilePicture = ai.image)
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = ai.code.uppercase(),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.onSecondaryContainer)
                                .padding(4.dp)
                        )
                    }
                    Text(
                        text = stringResource(id = ai.description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(onClick = onCreateChat) {
                Text(text = stringResource(id = R.string.chatbot_create_chat, name))
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