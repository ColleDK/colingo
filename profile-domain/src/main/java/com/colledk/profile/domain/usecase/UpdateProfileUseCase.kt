package com.colledk.profile.domain.usecase

import com.colledk.profile.domain.model.Profile
import com.colledk.profile.domain.repository.ProfileRepository

class UpdateProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(newProfile: Profile): Result<Unit> {
        return repository.updateProfile(newProfile = newProfile)
    }
}