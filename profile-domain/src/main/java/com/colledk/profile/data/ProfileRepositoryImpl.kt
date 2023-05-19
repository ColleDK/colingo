package com.colledk.profile.data

import com.colledk.profile.data.mapper.mapToDomain
import com.colledk.profile.data.mapper.mapToRemote
import com.colledk.profile.data.remote.ProfileRemoteDataSource
import com.colledk.profile.data.remote.model.ProfileRemote
import com.colledk.profile.domain.model.Profile
import com.colledk.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

class ProfileRepositoryImpl(
    private val remoteDataSource: ProfileRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): ProfileRepository {
    override suspend fun createProfile(profile: Profile): Result<Unit> = withContext(dispatcher) {
        val result = remoteDataSource.createProfile(profile = profile.mapToRemote())

        result.getOrElse {
            return@withContext Result.failure(exception = it)
        }

        Result.success(Unit)
    }

    override suspend fun getProfile(): Result<Profile> = withContext(dispatcher) {
        val result = remoteDataSource.getProfile()

        val profile: ProfileRemote? = result.getOrElse {
            return@withContext Result.failure(exception = it)
        }

        profile?.let {
            Result.success(
                it.mapToDomain()
            )
        } ?: run {
            Result.failure(IOException())
        }
    }

    override suspend fun updateProfile(newProfile: Profile): Result<Unit> = withContext(dispatcher) {
        val result = remoteDataSource.updateProfile(newProfile = newProfile.mapToRemote())

        result.getOrElse {
            return@withContext Result.failure(exception = it)
        }

        Result.success(Unit)
    }
}