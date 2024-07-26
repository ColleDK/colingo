package com.colledk.home.domain.model

import android.os.Parcelable
import com.colledk.user.domain.model.User
import kotlinx.parcelize.Parcelize
import org.joda.time.DateTime

@Parcelize
data class Reply(
    val id: String,
    val user: User,
    val content: String,
    val timestamp: DateTime,
    val replies: List<Reply>
) : Parcelable
