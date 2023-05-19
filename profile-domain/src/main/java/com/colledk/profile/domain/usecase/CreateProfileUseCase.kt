package com.colledk.profile.domain.usecase

import com.colledk.profile.domain.model.Profile
import com.colledk.profile.domain.repository.ProfileRepository

class CreateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(profile: Profile): Result<Unit> {
        return repository.createProfile(profile = profile)
    }
}