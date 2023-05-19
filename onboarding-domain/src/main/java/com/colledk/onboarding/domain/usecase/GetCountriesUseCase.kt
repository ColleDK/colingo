package com.colledk.onboarding.domain.usecase

import com.colledk.country.domain.model.Country
import com.colledk.onboarding.domain.repository.OnboardingRepository

class GetCountriesUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(): Result<List<Country>> {
        return repository.getCountries()
    }
}