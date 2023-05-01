package com.colledk.onboarding.domain.repository

import com.colledk.onboarding.domain.model.Country

interface OnboardingRepository {
    suspend fun getCountries(): Result<List<Country>>
}