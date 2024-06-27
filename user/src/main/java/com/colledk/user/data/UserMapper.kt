package com.colledk.user.data

import com.colledk.user.data.remote.model.GenderRemote
import com.colledk.user.data.remote.model.LanguageProficiencyRemote
import com.colledk.user.data.remote.model.LanguageRemote
import com.colledk.user.data.remote.model.LocationRemote
import com.colledk.user.data.remote.model.UserLanguageRemote
import com.colledk.user.data.remote.model.UserRemote
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.Language
import com.colledk.user.domain.model.LanguageProficiency
import com.colledk.user.domain.model.Location
import com.colledk.user.domain.model.User
import com.colledk.user.domain.model.UserLanguage

fun User.mapToRemote(): UserRemote {
    return UserRemote(
        name = name,
        birthday = birthday,
        profilePictures = profilePictures,
        description = description,
        location = location.mapToRemote(),
        languages = languages.map { it.mapToRemote() },
        friends = friends.map { it.mapToRemote() },
        gender = gender.mapToRemote()
    )
}

internal fun Location.mapToRemote(): LocationRemote {
    return LocationRemote(
        country = country,
        city = city
    )
}

internal fun UserLanguage.mapToRemote(): UserLanguageRemote {
    return UserLanguageRemote(
        language = language.mapToRemote(),
        proficiency = proficiency.mapToRemote()
    )
}

internal fun LanguageProficiency.mapToRemote(): LanguageProficiencyRemote {
    return LanguageProficiencyRemote.valueOf(this.name)
}

internal fun Language.mapToRemote(): LanguageRemote {
    return LanguageRemote(
        code = code,
        name = name,
        image = image
    )
}

internal fun Gender.mapToRemote(): GenderRemote {
    return GenderRemote.valueOf(this.name)
}

fun UserRemote.mapToDomain(): User {
    return User(
        name = name,
        birthday = birthday,
        profilePictures = profilePictures,
        description = description,
        location = location.mapToDomain(),
        languages = languages.map { it.mapToDomain() },
        friends = friends.map { it.mapToDomain() },
        gender = gender.mapToDomain()
    )
}

internal fun LocationRemote.mapToDomain(): Location {
    return Location(
        city = city,
        country = country
    )
}

internal fun UserLanguageRemote.mapToDomain(): UserLanguage {
    return UserLanguage(
        language = language.mapToDomain(),
        proficiency = proficiency.mapToDomain()
    )
}

internal fun LanguageRemote.mapToDomain(): Language {
    return Language(
        code = code,
        name = name,
        image = image
    )
}

internal fun LanguageProficiencyRemote.mapToDomain(): LanguageProficiency {
    return LanguageProficiency.valueOf(this.name)
}

internal fun GenderRemote.mapToDomain(): Gender {
    return Gender.valueOf(this.name)
}