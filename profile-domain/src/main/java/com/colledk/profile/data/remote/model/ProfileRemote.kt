package com.colledk.profile.data.remote.model

import com.colledk.country.data.remote.model.CountryRemote
import java.util.Date

data class ProfileRemote(
    val name: String,
    val profilePicUrl: String,
    val description: String?,
    val languagesKnown: List<CountryRemote>,
    val languagesLearning: List<CountryRemote>,
    val birthday: Date
)
