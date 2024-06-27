package com.colledk.chat.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.ui.uistates.ChatUiState

@Composable
internal fun ChatPane(
    state: ChatUiState,
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(state.chats) { chat ->
            ChatItem(chat = chat)
        }
    }
}

@Composable
private fun ChatItem(
    chat: Chat,
    modifier: Modifier = Modifier
) {

}