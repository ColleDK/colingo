package com.colledk.onboarding.ui.profilesetup.uistates

import android.location.Address
import androidx.compose.runtime.Stable
import org.joda.time.DateTime

@Stable
data class DescriptionUiState(
    val birthday: DateTime? = null,
    val address: Address? = null,
    val description: String = ""
)
