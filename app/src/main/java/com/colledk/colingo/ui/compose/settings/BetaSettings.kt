package com.colledk.colingo.ui.compose.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.fastForEach
import com.colledk.colingo.data.BetaSettings

@Composable
internal fun BetaSettings(
    modifier: Modifier = Modifier
) {
    val allSettings = remember {
        derivedStateOf {
            BetaSettings::class.sealedSubclasses.map { it.objectInstance as BetaSettings }
        }
    }

    allSettings.value.fastForEach { setting ->
        when(setting) {
            BetaSettings.ChatBotSetting -> {

            }
        }
    }
}

@Composable
private fun SettingItem(modifier: Modifier = Modifier) {

}