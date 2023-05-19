package com.colledk.onboarding.ui

import com.colledk.country.domain.model.Country

data class OnboardingUiState(
    val countries: List<Country> = listOf()
)
