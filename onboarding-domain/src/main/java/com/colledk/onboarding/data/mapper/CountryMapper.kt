package com.colledk.onboarding.data.mapper

import com.colledk.onboarding.data.remote.model.CountryRemote
import com.colledk.onboarding.domain.model.Country

const val COUNTRY_NAME_KEY = "country_name"
const val COUNTRY_FLAG_KEY = "country_flag"

fun CountryRemote.mapToDomain(): Country =
    Country(
        name = name,
        flag = flag
    )

fun CountryRemote.toHashMap(): HashMap<String, Any> {
    return hashMapOf(
        COUNTRY_NAME_KEY to name,
        COUNTRY_FLAG_KEY to flag
    )
}

fun Map<String, Any>.toCountryRemote(): CountryRemote {
    return CountryRemote(
        name = this[COUNTRY_NAME_KEY].toString(),
        flag = this[COUNTRY_FLAG_KEY].toString()
    )
}