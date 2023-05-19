package com.colledk.profile.domain.model

import com.colledk.country.domain.model.Country
import java.util.Date

data class Profile(
    val name: String,
    val profilePicUrl: String,
    val description: String?,
    val languagesKnown: List<Country>,
    val languagesLearning: List<Country>,
    val birthday: Date
)
