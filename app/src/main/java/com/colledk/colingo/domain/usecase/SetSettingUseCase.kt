package com.colledk.colingo.domain.usecase

import com.colledk.colingo.data.Setting
import com.colledk.colingo.domain.repository.SettingsRepository

class SetSettingUseCase(private val repository: SettingsRepository) {
    suspend operator fun<T> invoke(setting: Setting<T>, value: Any) {
        (value as? T)?.let {
            return repository.setSetting(setting = setting, value = it)
        }
    }
}