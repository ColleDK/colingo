package com.colledk.user.di

import com.colledk.user.data.remote.UserRemoteDataSource
import com.colledk.user.data.remote.repository.UserRepositoryImpl
import com.colledk.user.domain.repository.UserRepository
import com.colledk.user.domain.usecase.CreateUserUseCase
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.LoginUseCase
import com.colledk.user.domain.usecase.UpdateUserUseCase
import com.colledk.user.domain.usecase.UploadProfilePicUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
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
            db = Firebase.firestore,
            auth = Firebase.auth,
            storage = Firebase.storage
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
    fun providesCreateUserUseCase(repository: UserRepository): CreateUserUseCase {
        return CreateUserUseCase(repository = repository)
    }

    @Provides
    fun providesLoginUseCase(repository: UserRepository): LoginUseCase {
        return LoginUseCase(repository = repository)
    }

    @Provides
    fun providesGetCurrentUserUseCase(
        repository: UserRepository
    ): GetCurrentUserUseCase {
        return GetCurrentUserUseCase(repository = repository, auth = Firebase.auth)
    }

    @Provides
    fun providesUpdateUserUseCase(
        repository: UserRepository
    ) : UpdateUserUseCase {
        return UpdateUserUseCase(repository = repository)
    }

    @Provides
    fun providesUploadProfilePictureUseCase(
        repository: UserRepository
    ) : UploadProfilePicUseCase {
        return UploadProfilePicUseCase(repository = repository)
    }
}