package com.colledk.colingo.ui.compose.settings

import androidx.compose.material3.ListItem
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SettingItem(
    text: String,
    value: Any?,
    onValueChange: (Any) -> Unit,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(text = text)
        },
        trailingContent = {
            when (value) {
                is Boolean -> {
                    BooleanSetting(
                        value = value,
                        onValueChange = onValueChange,
                        modifier = modifier
                    )
                }
                else -> {

                }
            }
        }
    )
}

@Composable
fun BooleanSetting(
    value: Boolean,
    onValueChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(checked = value, onCheckedChange = onValueChange, modifier = modifier)
}