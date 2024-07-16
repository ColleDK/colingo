package com.colledk.user.domain.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime
import java.util.Date

@Parcelize
data class User(
    val id: String = "",
    val name: String = "",
    val birthday: DateTime = DateTime.now(),
    val profilePictures: List<Uri> = emptyList(),
    val description: String = "",
    val location: Location = Location(),
    val languages: List<UserLanguage> = emptyList(),
    val gender: Gender = Gender.OTHER,
    val chats: List<String> = emptyList(),
    val aiChats: List<String> = emptyList(),
    val topics: List<Topic> = emptyList()
) : Parcelable
