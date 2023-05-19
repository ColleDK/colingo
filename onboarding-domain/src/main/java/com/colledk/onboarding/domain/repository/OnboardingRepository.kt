package com.colledk.onboarding.domain.repository

import com.colledk.country.domain.model.Country

interface OnboardingRepository {
    suspend fun getCountries(): Result<List<Country>>

    suspend fun createUser(email: String, password: String): Result<Unit>
}