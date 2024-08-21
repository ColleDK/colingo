package com.colledk.user.domain.model

import android.location.Address
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val country: String = "",
    val city: String = ""
): Parcelable {
    fun isUnknown(): Boolean {
        return country.isBlank() && city.isBlank()
    }

    override fun toString(): String {
        return "$country, $city"
    }
}

fun Address.isUnknown(): Boolean {
    return countryName.isNullOrBlank() && locality.isNullOrBlank()
}

fun Address.toText(): String {
    return "$countryName, $locality"
}