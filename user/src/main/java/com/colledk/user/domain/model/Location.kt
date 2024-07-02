package com.colledk.user.domain.model

data class Location(
    val country: String = "",
    val city: String = ""
) {
    override fun toString(): String {
        return "$country, $city"
    }
}
