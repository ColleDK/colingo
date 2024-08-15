package com.colledk.profile.ui.uistates

import android.os.Parcelable
import com.colledk.user.domain.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class EditProfileUiState(val user: User): Parcelable
