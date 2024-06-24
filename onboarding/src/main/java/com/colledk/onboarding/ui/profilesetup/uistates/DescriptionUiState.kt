package com.colledk.onboarding.ui.profilesetup.uistates

import androidx.compose.runtime.Stable
import java.time.LocalDate

@Stable
data class DescriptionUiState(
    val birthday: LocalDate? = null,
    val location: String? = null,
    val description: String = ""
)
