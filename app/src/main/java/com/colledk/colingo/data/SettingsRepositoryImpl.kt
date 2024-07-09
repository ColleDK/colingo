package com.colledk.colingo.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.colledk.colingo.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsRepositoryImpl(
    private val dataStore: DataStore<Preferences>
) : SettingsRepository {
    override suspend fun<T> getSetting(setting: Setting<T>): Flow<T> = dataStore.data.map { preference ->
        preference[setting.field] ?: setting.defaultValue
    }

    override suspend fun <T> setSetting(setting: Setting<T>, value: T) {
        dataStore.edit { preference ->
            preference[setting.field] = value
        }
    }
}