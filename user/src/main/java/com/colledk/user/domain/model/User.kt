package com.colledk.user.domain.model

import android.net.Uri
import java.util.Date

data class User(
    val id: String,
    val name: String,
    val birthday: Long,
    val profilePictures: List<Uri>,
    val description: String,
    val location: Location,
    val languages: List<UserLanguage>,
    val gender: Gender,
    val friends: List<User>,
    val chats: List<String>
) {
    companion object {
        val UNKNOWN = User(
            id = "",
            name = "",
            birthday = Date().time,
            profilePictures = emptyList(),
            description = "",
            location = Location(country = "", city = ""),
            languages = emptyList(),
            gender = Gender.OTHER,
            friends = emptyList(),
            chats = emptyList()
        )
    }
}
