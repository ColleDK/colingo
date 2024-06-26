package com.colledk.onboarding.ui.profilesetup.uistates

import androidx.compose.runtime.Stable
import com.colledk.user.domain.model.UserLanguage

@Stable
data class LanguagesUiState(
    val languages: List<UserLanguage> = emptyList()
)
