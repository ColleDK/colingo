package com.colledk.user.data.remote.model

import android.net.Uri

data class UserRemote(
    val name: String,
    val birthday: Long,
    val profilePictures: List<Uri>,
    val description: String,
    val location: LocationRemote,
    val languages: List<UserLanguageRemote>,
    val gender: GenderRemote,
    val friends: List<UserRemote>
)
