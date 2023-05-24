package com.colledk.chat.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.colledk.chat.ui.R
import com.colledk.chat.ui.model.Chat

@Composable
internal fun ChatListItem(
    chat: Chat,
    navigateToProfile: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(
                color = MaterialTheme.colorScheme.secondary,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (chat.user.profilePicUrl.isEmpty()) {
            IconButton(
                onClick = navigateToProfile,
                modifier = Modifier.clickable(
                    onClick = navigateToProfile,
                    onClickLabel = stringResource(
                        id = R.string.chat_screen_navigate_to_profile,
                        formatArgs = arrayOf(
                            chat.user.name
                        )
                    )
                )
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = "Profile picture of ${chat.user.name}",
                    modifier = Modifier
                        .requiredSizeIn(
                            minWidth = 48.dp,
                            minHeight = 48.dp
                        )
                        .clip(CircleShape),
                )
            }
        } else {
            IconButton(
                onClick = navigateToProfile, modifier = Modifier.clickable(
                    onClick = navigateToProfile,
                    onClickLabel = stringResource(
                        id = R.string.chat_screen_navigate_to_profile,
                        formatArgs = arrayOf(
                            chat.user.name
                        )
                    )
                )
            ) {
                AsyncImage(
                    model = chat.user.profilePicUrl,
                    contentDescription = "Profile picture of ${chat.user.name}"
                )
            }
        }

        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = chat.user.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = chat.messages.lastOrNull()?.message ?: "No messages yet",
                style = MaterialTheme.typography.labelSmall
            )
        }

        Text(
            text = chat.messages.lastOrNull()?.timestamp?.toString() ?: "",
            style = MaterialTheme.typography.labelSmall
        )
    }
}