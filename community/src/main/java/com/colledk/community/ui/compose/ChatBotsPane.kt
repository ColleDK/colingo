package com.colledk.community.ui.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
internal fun ChatBotsPane(
    onCreateAiChat: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Button(onClick = onCreateAiChat) {
            Text(text = "Create a new ai chat")
        }
    }
}