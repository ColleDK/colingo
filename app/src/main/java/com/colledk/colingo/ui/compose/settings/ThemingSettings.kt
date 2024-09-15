package com.colledk.colingo.ui.compose.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.colledk.colingo.R
import com.colledk.colingo.data.ThemingSettings
import com.colledk.colingo.data.Setting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ThemingSettingsPane(
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

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_theming_title),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    ) {
        when (settingUiState) {
            is SettingUiState.Data -> {
                Column(
                    modifier = Modifier.padding(it)
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
                Column(
                    modifier = Modifier.fillMaxSize().padding(it)
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}