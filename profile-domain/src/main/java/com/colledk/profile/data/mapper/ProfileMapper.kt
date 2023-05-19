package com.colledk.profile.data.mapper

import com.colledk.country.data.mapper.mapToDomain
import com.colledk.country.data.mapper.mapToRemote
import com.colledk.profile.data.remote.model.ProfileRemote
import com.colledk.profile.domain.model.Profile

fun ProfileRemote.mapToDomain(): Profile =
    Profile(
        name = name,
        profilePicUrl = profilePicUrl,
        description = description,
        languagesKnown = languagesKnown.map { it.mapToDomain() },
        languagesLearning = languagesLearning.map { it.mapToDomain() },
        birthday = birthday
    )

fun Profile.mapToRemote(): ProfileRemote =
    ProfileRemote(
        name = name,
        profilePicUrl = profilePicUrl,
        description = description,
        languagesKnown = languagesKnown.map { it.mapToRemote() },
        languagesLearning = languagesLearning.map { it.mapToRemote() },
        birthday = birthday
    )