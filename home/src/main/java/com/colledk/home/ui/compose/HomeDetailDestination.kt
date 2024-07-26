package com.colledk.home.ui.compose

import android.os.Parcelable
import com.colledk.home.domain.model.Post
import com.colledk.user.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class HomeDetailDestination : Parcelable {
    data class ProfileDestination(val user: User): HomeDetailDestination()
    data class PostDestination(val postId: String): HomeDetailDestination()
}