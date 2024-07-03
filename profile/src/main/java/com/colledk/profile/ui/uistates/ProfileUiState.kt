package com.colledk.profile.ui.uistates

import androidx.compose.runtime.Stable
import com.colledk.user.domain.model.User

sealed class ProfileUiState() {
    data object Loading: ProfileUiState()

    @Stable
    data class Data(val currentUser: User) : ProfileUiState()
}

