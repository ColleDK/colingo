package com.colledk.user.data

import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import com.colledk.user.data.remote.model.GenderRemote
import com.colledk.user.data.remote.model.LanguageProficiencyRemote
import com.colledk.user.data.remote.model.AddressRemote
import com.colledk.user.data.remote.model.TopicRemote
import com.colledk.user.data.remote.model.UserLanguageRemote
import com.colledk.user.data.remote.model.UserRemote
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.LanguageProficiency
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.User
import com.colledk.user.domain.model.UserLanguage
import org.joda.time.DateTime
import java.util.Locale

fun User.mapToRemote(): UserRemote {
    return UserRemote(
        id = id,
        name = name,
        birthday = birthday.millis,
        profilePictures = profilePictures.map { it.toString() },
        description = description,
        location = address.mapToRemote(),
        languages = languages.map { it.mapToRemote() },
        gender = gender.mapToRemote(),
        chats = chats,
        topics = topics.map { it.mapToRemote() },
        aiChats = aiChats
    )
}

fun Topic.mapToRemote(): TopicRemote {
    return TopicRemote(this.name)
}

internal fun Address.mapToRemote(): AddressRemote {
    return AddressRemote(
        longitude = longitude,
        latitude = latitude
    )
}

internal fun UserLanguage.mapToRemote(): UserLanguageRemote {
    return UserLanguageRemote(
        code = language.language,
        proficiency = proficiency.mapToRemote()
    )
}

internal fun LanguageProficiency.mapToRemote(): LanguageProficiencyRemote {
    return LanguageProficiencyRemote.valueOf(this.name)
}

internal fun Gender.mapToRemote(): GenderRemote {
    return GenderRemote.valueOf(this.name)
}

fun UserRemote.mapToDomain(geocoder: Geocoder): User {
    return User(
        id = id,
        name = name,
        birthday = DateTime(birthday),
        profilePictures = profilePictures.map { Uri.parse(it) },
        description = description,
        address = location.mapToDomain(geocoder = geocoder),
        languages = languages.map { it.mapToDomain() },
        gender = gender.mapToDomain(),
        chats = chats,
        topics = topics.map { it.mapToDomain() },
        aiChats = aiChats
    )
}

fun TopicRemote.mapToDomain(): Topic {
    return Topic.valueOf(this.name)
}

internal fun AddressRemote.mapToDomain(geocoder: Geocoder): Address {
    return geocoder.getFromLocation(
        latitude,
        longitude,
        1
    )?.firstOrNull() ?: Address(Locale.getDefault())
}

internal fun UserLanguageRemote.mapToDomain(): UserLanguage {
    return UserLanguage(
        language = Locale.forLanguageTag(this.code),
        proficiency = proficiency.mapToDomain()
    )
}

internal fun LanguageProficiencyRemote.mapToDomain(): LanguageProficiency {
    return LanguageProficiency.valueOf(this.name)
}

internal fun GenderRemote.mapToDomain(): Gender {
    return Gender.valueOf(this.name)
}