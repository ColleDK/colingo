package com.colledk.colingo.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.colledk.colingo.data.SettingsRepositoryImpl
import com.colledk.colingo.domain.repository.SettingsRepository
import com.colledk.colingo.domain.usecase.GetSettingUseCase
import com.colledk.colingo.domain.usecase.SetSettingUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class SettingsModule {
    @Provides
    fun providesSettingRepository(
        dataStore: DataStore<Preferences>
    ): SettingsRepository {
        return SettingsRepositoryImpl(dataStore = dataStore)
    }

    @Provides
    fun providesGetSettingUseCase(
        repository: SettingsRepository
    ): GetSettingUseCase {
        return GetSettingUseCase(repository = repository)
    }

    @Provides
    fun providesSetSettingUseCase(
        repository: SettingsRepository
    ): SetSettingUseCase {
        return SetSettingUseCase(repository = repository)
    }
}