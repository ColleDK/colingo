package com.colledk.user.domain.model

import android.net.Uri

data class User(
    val name: String,
    val birthday: Long,
    val profilePictures: List<Uri>,
    val description: String,
    val location: Location,
    val languages: List<UserLanguage>,
    val gender: Gender,
    val friends: List<User>,
    val chats: List<String>
)
