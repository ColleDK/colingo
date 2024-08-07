package com.colledk.profile.ui.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.colledk.user.domain.model.Topic

@Composable
internal fun ProfileTopics(
    topics: List<Topic>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Interests",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        topics.fastForEach { topic ->
            TopicItem(topic = topic, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun TopicItem(
    topic: Topic,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(id = topic.topicName),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        },
        trailingContent = {
            Icon(
                painter = painterResource(id = topic.icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        modifier = modifier.clip(RoundedCornerShape(4.dp)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}