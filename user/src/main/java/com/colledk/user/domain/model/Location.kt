package com.colledk.user.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val country: String = "",
    val city: String = ""
): Parcelable {
    override fun toString(): String {
        return "$country, $city"
    }
}
