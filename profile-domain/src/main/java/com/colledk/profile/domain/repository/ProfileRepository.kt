package com.colledk.profile.domain.repository

import com.colledk.profile.domain.model.Profile

interface ProfileRepository {
    suspend fun createProfile(profile: Profile): Result<Unit>

    suspend fun getProfile(): Result<Profile>

    suspend fun updateProfile(newProfile: Profile): Result<Unit>
}