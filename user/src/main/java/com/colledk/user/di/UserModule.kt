package com.colledk.user.di

import com.colledk.user.data.remote.UserRemoteDataSource
import com.colledk.user.data.remote.repository.UserRepositoryImpl
import com.colledk.user.domain.repository.UserRepository
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {
    @Provides
    fun providesUserRemoteDataSource(): UserRemoteDataSource {
        return UserRemoteDataSource(
            db = FirebaseFirestore.getInstance(),
            auth = Firebase.auth
        )
    }

    @Provides
    @Singleton
    fun providesUserRepository(
        remoteDataSource: UserRemoteDataSource
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource = remoteDataSource)
    }

    @Provides
    fun providesGetCurrentUserUseCase(
        repository: UserRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository = repository, auth = Firebase.auth)
    }
}