package com.colledk.colingo.hilt

import com.colledk.onboarding.data.OnboardingRepositoryImpl
import com.colledk.onboarding.data.remote.OnboardingRemoteDataSource
import com.colledk.onboarding.domain.repository.OnboardingRepository
import com.colledk.onboarding.domain.usecase.CreateUserUseCase
import com.colledk.onboarding.domain.usecase.GetCountriesUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class OnboardingModule {

    // Remote data source
    @Provides
    fun providesOnboardingRemoteDataSource(): OnboardingRemoteDataSource {
        return OnboardingRemoteDataSource()
    }

    // Repo
    @Provides
    @Singleton
    fun providesOnboardingRepository(
        remoteDataSource: OnboardingRemoteDataSource
    ): OnboardingRepository {
        return OnboardingRepositoryImpl(
            remoteDataSource = remoteDataSource
        )
    }

    // Use cases
    @Provides
    fun providesCreateUserUseCase(
        repository: OnboardingRepository
    ): CreateUserUseCase {
        return CreateUserUseCase(repository = repository)
    }

    @Provides
    fun providesGetCountriesUseCase(
        repository: OnboardingRepository
    ): GetCountriesUseCase {
        return GetCountriesUseCase(repository = repository)
    }

}