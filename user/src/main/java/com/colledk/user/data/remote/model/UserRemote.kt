package com.colledk.user.data.remote.model

import android.net.Uri

data class UserRemote(
    val id: String = "",
    val name: String = "",
    val birthday: Long = 0L,
    val profilePictures: List<String> = emptyList(),
    val description: String = "",
    val location: LocationRemote = LocationRemote(),
    val languages: List<UserLanguageRemote> = emptyList(),
    val gender: GenderRemote = GenderRemote.OTHER,
    val friends: List<UserRemote> = emptyList(),
    val chats: List<String> = emptyList()
)
