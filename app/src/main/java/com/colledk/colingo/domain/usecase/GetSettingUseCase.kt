package com.colledk.colingo.domain.usecase

import com.colledk.colingo.data.Setting
import com.colledk.colingo.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow

class GetSettingUseCase(private val repository: SettingsRepository) {
    suspend operator fun<T> invoke(setting: Setting<T>): Flow<T> {
        return repository.getSetting(setting = setting)
    }
}