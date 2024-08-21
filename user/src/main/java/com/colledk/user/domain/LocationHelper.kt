package com.colledk.user.domain

import android.content.Context
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getGeocoder(): Geocoder {
        return Geocoder(context, Locale.getDefault())
    }
}