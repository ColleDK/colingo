package com.colledk.common

import android.app.GrammaticalInflectionManager
import android.content.Context
import android.content.res.Configuration
import android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE
import androidx.annotation.RequiresApi
import dagger.hilt.android.qualifiers.ApplicationContext

class LocaleHelper(@ApplicationContext private val context: Context) {
    @RequiresApi(UPSIDE_DOWN_CAKE)
    fun updateLocaleGender(gender: Int = Configuration.GRAMMATICAL_GENDER_NEUTRAL) {
        context.getSystemService(GrammaticalInflectionManager::class.java)
            .setRequestedApplicationGrammaticalGender(gender)
    }
}