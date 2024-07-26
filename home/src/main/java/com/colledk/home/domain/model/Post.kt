package com.colledk.home.domain.model

import android.os.Parcelable
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.User
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
data class Post(
    val id: String,
    val user: User,
    val content: String,
    val replies: List<Reply>,
    val likes: List<String>,
    val timestamp: DateTime,
    val topics: List<Topic>
): Parcelable
