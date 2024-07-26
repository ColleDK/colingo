package com.colledk.colingo.ui

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
//    private val datastore: DataStore<Preferences>
) : ViewModel() {
    fun switchLanguages(locale: Locale) {
        Timber.d("Switching languages $locale")
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.create(locale))
    }
}