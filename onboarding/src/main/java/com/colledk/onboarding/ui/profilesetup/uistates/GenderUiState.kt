package com.colledk.onboarding.ui.profilesetup.uistates

import androidx.compose.runtime.Stable
import com.colledk.onboarding.domain.Gender

@Stable
data class GenderUiState(
    val selectedGender: Gender? = null
)
