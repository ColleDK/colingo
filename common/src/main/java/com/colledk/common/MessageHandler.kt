package com.colledk.common

import android.content.Context
import androidx.annotation.StringRes
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

class MessageHandler @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val _messages: Channel<String> = Channel(Channel.BUFFERED)
    val messages: Flow<String> = _messages.receiveAsFlow()

    fun displayMessage(message: String) {
        _messages.trySend(message)
    }

    fun displayMessage(@StringRes id: Int, vararg args: Any) {
        displayMessage(message = context.getString(id, *args))
    }

    fun displayError(error: Throwable, fallback: String) {
        displayMessage(message = error.message ?: fallback)
    }

    fun displayError(error: Throwable, @StringRes fallback: Int, vararg args: Any) {
        displayError(
            error = error,
            fallback = context.getString(fallback, *args)
        )
    }

    fun displayError(error: Throwable) {
        displayError(
            error = error,
            fallback = R.string.default_error
        )
    }
}