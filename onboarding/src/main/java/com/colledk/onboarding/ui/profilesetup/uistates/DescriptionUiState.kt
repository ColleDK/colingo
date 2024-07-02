package com.colledk.onboarding.ui.profilesetup.uistates

import androidx.compose.runtime.Stable
import com.colledk.user.domain.model.Location
import org.joda.time.DateTime
import java.time.LocalDate

@Stable
data class DescriptionUiState(
    val birthday: DateTime? = null,
    val location: Location? = null,
    val description: String = ""
)
