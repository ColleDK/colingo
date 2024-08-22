package com.colledk.user.domain

import android.content.Context
import android.location.Address
import android.location.Geocoder
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

class LocationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun getGeocoder(): Geocoder {
        return Geocoder(context, Locale.getDefault())
    }
}

@OptIn(ExperimentalContracts::class)
fun isAddressUnknown(address: Address?): Boolean {
    contract { returns(false) implies (address != null) }
    return when(address) {
        null -> true
        else -> address.isUnknown()
    }
}


@OptIn(ExperimentalContracts::class)
fun Address?.isUnknown(): Boolean {
    contract { returns(false) implies (this@isUnknown != null) }
    return when(this) {
        null -> true
        else -> countryName.isNullOrBlank() && locality.isNullOrBlank()
    }
}

fun Address.toText(): String {
    return "$countryName, $locality"
}