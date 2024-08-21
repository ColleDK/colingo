package com.colledk.user.di

import android.content.Context
import android.location.Geocoder
import com.colledk.user.data.remote.UserRemoteDataSource
import com.colledk.user.data.remote.repository.UserRepositoryImpl
import com.colledk.user.domain.LocationHelper
import com.colledk.user.domain.pagination.UserPagingSource
import com.colledk.user.domain.repository.UserRepository
import com.colledk.user.domain.usecase.AddAiChatUseCase
import com.colledk.user.domain.usecase.AddProfilePictureUseCase
import com.colledk.user.domain.usecase.CreateUserUseCase
import com.colledk.user.domain.usecase.DeleteProfilePictureUseCase
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.GetUserUseCase
import com.colledk.user.domain.usecase.LoginUseCase
import com.colledk.user.domain.usecase.UpdateUserUseCase
import com.colledk.user.domain.usecase.UploadProfilePicUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class UserModule {
    @Provides
    fun providesUserRemoteDataSource(
        db: FirebaseFirestore,
        auth: FirebaseAuth,
        storage: FirebaseStorage
    ): UserRemoteDataSource {
        return UserRemoteDataSource(
            db = db,
            auth = auth,
            storage = storage
        )
    }

    @Provides
    @Singleton
    fun providesUserRepository(
        remoteDataSource: UserRemoteDataSource,
        locationHelper: LocationHelper
    ): UserRepository {
        return UserRepositoryImpl(remoteDataSource = remoteDataSource, locationHelper = locationHelper)
    }

    @Provides
    fun providesUserPagingSource(
        locationHelper: LocationHelper,
        db: FirebaseFirestore,
        auth: FirebaseAuth,
    ): UserPagingSource {
        return UserPagingSource(db = db, auth = auth, locationHelper = locationHelper)
    }

    @Provides
    @Singleton
    fun providesLocationHelper(
        @ApplicationContext context: Context
    ): LocationHelper {
        return LocationHelper(context = context)
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

    @Provides
    fun providesDeleteProfilePictureUseCase(
        repository: UserRepository
    ): DeleteProfilePictureUseCase {
        return DeleteProfilePictureUseCase(repository = repository)
    }

    @Provides
    fun providesGetUserUseCase(
        repository: UserRepository
    ): GetUserUseCase {
        return GetUserUseCase(repository = repository)
    }

    @Provides
    fun providesAddAiChatUseCase(
        repository: UserRepository
    ) : AddAiChatUseCase {
        return AddAiChatUseCase(repository = repository)
    }

    @Provides
    fun providesAddProfilePictureUseCase(
        repository: UserRepository
    ) : AddProfilePictureUseCase {
        return AddProfilePictureUseCase(repository = repository)
    }
}