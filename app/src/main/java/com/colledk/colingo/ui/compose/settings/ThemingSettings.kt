package com.colledk.colingo.ui.compose.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.colledk.colingo.data.ThemingSettings
import com.colledk.colingo.data.Setting

@Composable
internal fun ThemingSettings(
    settingUiState: SettingUiState,
    retrieveSettings: (settings: List<Setting<*>>) -> Unit,
    updateSetting: (setting: Setting<*>, newValue: Any) -> Unit,
    modifier: Modifier = Modifier
) {
    val allSettings = remember {
        derivedStateOf {
            ThemingSettings::class.sealedSubclasses.map { it.objectInstance as ThemingSettings }
                .map { it.setting }
        }
    }

    LaunchedEffect(key1 = allSettings.value) {
        retrieveSettings(allSettings.value)
    }

    when (settingUiState) {
        is SettingUiState.Data -> {
            Column(
                modifier = modifier
            ) {
                settingUiState.settings.forEach { (setting, flow) ->
                    val currentValue by flow.collectAsState(null)
                    SettingItem(
                        text = stringResource(id = setting.textRes),
                        value = currentValue,
                        onValueChange = {
                            updateSetting(setting, it)
                        }
                    )
                }
            }
        }

        SettingUiState.Loading -> {

        }
    }
}