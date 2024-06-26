package com.colledk.chat.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier

@Composable
internal fun ChatPane(
    modifier: Modifier = Modifier,
    onNavigate: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        onNavigate()
    }
}