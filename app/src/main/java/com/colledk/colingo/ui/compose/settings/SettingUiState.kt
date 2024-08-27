package com.colledk.colingo.ui.compose.settings

import com.colledk.colingo.data.Setting
import kotlinx.coroutines.flow.Flow

sealed class SettingUiState {
    data object Loading: SettingUiState()
    data class Data(val settings: Map<Setting<*>, Flow<*>>) : SettingUiState()
}

