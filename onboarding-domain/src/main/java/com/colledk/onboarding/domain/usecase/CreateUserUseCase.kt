package com.colledk.onboarding.domain.usecase

import com.colledk.onboarding.domain.repository.OnboardingRepository

class CreateUserUseCase(
    private val repository: OnboardingRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> {
        return repository.createUser(
            email = email,
            password = password
        )
    }
}