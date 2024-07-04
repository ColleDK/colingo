package com.colledk.colingo.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.colledk.colingo.data.Setting
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun<T> getSetting(setting: Setting<T>): Flow<T>
    suspend fun<T> setSetting(setting: Setting<T>, value: T)
}