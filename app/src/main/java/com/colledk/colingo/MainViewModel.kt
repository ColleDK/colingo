package com.colledk.colingo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.colingo.data.ThemingSettings
import com.colledk.colingo.domain.usecase.GetSettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getSettingUseCase: GetSettingUseCase
) : ViewModel() {
    private val _dynamicThemeEnabled: MutableStateFlow<Boolean?> = MutableStateFlow(null)
    val dynamicThemeEnabled: StateFlow<Boolean?> = _dynamicThemeEnabled

    private val _darkThemeEnabled: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val darkThemeEnabled: StateFlow<Boolean> = _darkThemeEnabled

    init {
        getDynamicThemeEnabled()
        getDarkThemeEnabled()
    }

    private fun getDynamicThemeEnabled() {
        viewModelScope.launch {
            getSettingUseCase(ThemingSettings.DynamicThemeSetting.setting).collectLatest { value ->
                _dynamicThemeEnabled.update { value as Boolean }
            }
        }
    }

    private fun getDarkThemeEnabled() {
        viewModelScope.launch {
            getSettingUseCase(ThemingSettings.DarkThemeSetting.setting).collectLatest { value ->
                _darkThemeEnabled.update { value as Boolean }
            }
        }
    }
}