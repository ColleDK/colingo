package com.colledk.colingo.data

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

data class Setting<T>(
    val field: Preferences.Key<T>,
    val defaultValue: T
)

sealed class BetaSettings(val setting: Setting<*>) {
    data object ChatBotSetting : BetaSettings(
        setting = Setting(
            field = booleanPreferencesKey("chatbot"),
            defaultValue = false
        )
    )
}
