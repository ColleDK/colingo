package com.colledk.onboarding.data

import com.colledk.onboarding.data.mapper.mapToDomain
import com.colledk.onboarding.data.remote.OnboardingRemoteDataSource
import com.colledk.onboarding.data.remote.model.CountryRemote
import com.colledk.onboarding.domain.model.Country
import com.colledk.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OnboardingRepositoryImpl(
    private val remoteDataSource: OnboardingRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): OnboardingRepository {
    override suspend fun getCountries(): Result<List<Country>> = withContext(dispatcher) {
        val result = remoteDataSource.getCountries()
        val countries: List<CountryRemote> = result.getOrElse {
            return@withContext Result.failure(it)
        }

        return@withContext Result.success(countries.map { it.mapToDomain() })
    }
}