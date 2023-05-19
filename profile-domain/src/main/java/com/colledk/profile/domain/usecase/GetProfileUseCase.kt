package com.colledk.profile.domain.usecase

import com.colledk.profile.domain.model.Profile
import com.colledk.profile.domain.repository.ProfileRepository

class GetProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(): Result<Profile> {
        return repository.getProfile()
    }
}