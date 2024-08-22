package com.colledk.colingo.ui

import androidx.lifecycle.ViewModel
import com.colledk.common.MessageHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor(
    private val messageHandler: MessageHandler
) : ViewModel() {
    val messages: Flow<String> = messageHandler.messages
}