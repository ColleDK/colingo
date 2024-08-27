package com.colledk.colingo.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.colingo.data.Setting
import com.colledk.colingo.domain.model.TopSetting
import com.colledk.colingo.domain.usecase.GetSettingUseCase
import com.colledk.colingo.domain.usecase.SetSettingUseCase
import com.colledk.colingo.ui.compose.settings.SettingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject
import kotlin.reflect.typeOf

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingUseCase: GetSettingUseCase,
    private val setSettingUseCase: SetSettingUseCase,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val _uiState: MutableStateFlow<SettingUiState> = MutableStateFlow(SettingUiState.Loading)
    val uiState: StateFlow<SettingUiState> = _uiState
    val selectedSetting: StateFlow<TopSetting?> = savedStateHandle.getStateFlow("setting", savedStateHandle["setting"])

    fun selectSetting(setting: TopSetting) {
        savedStateHandle["setting"] = setting
    }

    fun switchLanguages(locale: Locale) {
        Timber.d("Switching languages $locale")
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }

    fun getSettings(settings: List<Setting<*>>) {
        viewModelScope.launch {
            _uiState.value = SettingUiState.Loading

            val result = mutableMapOf<Setting<*>, Flow<*>>()

            settings.map {
                async {
                    result.put(it, getSettingUseCase(setting = it))
                }
            }.awaitAll()

            _uiState.value = SettingUiState.Data(settings = result)
        }
    }

    fun updateSetting(setting: Setting<*>, value: Any) {
        viewModelScope.launch {
            setSettingUseCase(setting = setting, value = value)
        }
    }
}