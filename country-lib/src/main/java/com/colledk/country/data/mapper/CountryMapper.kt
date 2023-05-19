package com.colledk.country.data.mapper

import com.colledk.country.data.remote.model.CountryRemote
import com.colledk.country.domain.model.Country

fun CountryRemote.mapToDomain(): Country =
    Country(
        name = name,
        flag = flag
    )

fun Country.mapToRemote(): CountryRemote =
    CountryRemote(
        name = name,
        flag = flag
    )