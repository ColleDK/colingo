package com.colledk.user.domain.model

import android.net.Uri
import org.joda.time.DateTime
import java.util.Date

data class User(
    val id: String,
    val name: String,
    val birthday: DateTime,
    val profilePictures: List<Uri>,
    val description: String,
    val location: Location,
    val languages: List<UserLanguage>,
    val gender: Gender,
    val chats: List<String>,
) {
    companion object {
        val UNKNOWN = User(
            id = "",
            name = "",
            birthday = DateTime.now(),
            profilePictures = emptyList(),
            description = "",
            location = Location(country = "", city = ""),
            languages = emptyList(),
            gender = Gender.OTHER,
            chats = emptyList()
        )
    }
}
