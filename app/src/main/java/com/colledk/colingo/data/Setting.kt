package com.colledk.colingo.data

import androidx.annotation.StringRes
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import com.colledk.colingo.R

data class Setting<T>(
    @StringRes val textRes: Int,
    val field: Preferences.Key<T>,
    val defaultValue: T
)

sealed class ThemingSettings(val setting: Setting<*>) {
    data object DynamicThemeSetting : ThemingSettings(
        setting = Setting(
            textRes = R.string.setting_dynamic_theme,
            field = booleanPreferencesKey("dynamic-theme"),
            defaultValue = true
        )
    )

    data object DarkThemeSetting: ThemingSettings(
        setting = Setting(
            textRes = R.string.setting_dark_theme,
            field = booleanPreferencesKey("dark-theme"),
            defaultValue = true
        )
    )
}
