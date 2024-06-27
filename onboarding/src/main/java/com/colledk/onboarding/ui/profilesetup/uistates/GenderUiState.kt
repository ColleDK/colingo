package com.colledk.onboarding.ui.profilesetup.uistates

import androidx.compose.runtime.Stable
import com.colledk.user.domain.model.Gender

@Stable
data class GenderUiState(
    val selectedGender: Gender? = null
)
