package com.colledk.home.di

import android.content.Context
import com.colledk.home.data.remote.HomeRemoteDataSource
import com.colledk.home.data.remote.repository.HomeRepositoryImpl
import com.colledk.home.domain.repository.HomeRepository
import com.colledk.home.domain.usecase.AddReplyUseCase
import com.colledk.home.domain.usecase.CreatePostUseCase
import com.colledk.home.domain.usecase.FormatNumberUseCase
import com.colledk.home.domain.usecase.GetPostUseCase
import com.colledk.home.domain.usecase.LikePostUseCase
import com.colledk.home.domain.usecase.RemovePostLikeUseCase
import com.colledk.home.domain.usecase.UpdatePostUseCase
import com.colledk.user.domain.repository.UserRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class HomeModule {
    @Provides
    fun providesHomeRemoteDataSource(
        db: FirebaseFirestore
    ): HomeRemoteDataSource {
        return HomeRemoteDataSource(db = db)
    }

    @Provides
    @Singleton
    fun providesHomeRepository(
        userRepository: UserRepository,
        remoteDataSource: HomeRemoteDataSource
    ): HomeRepository {
        return HomeRepositoryImpl(
            userRepository = userRepository,
            remoteDataSource = remoteDataSource
        )
    }

    @Provides
    fun providesCreatePostUseCase(repository: HomeRepository): CreatePostUseCase {
        return CreatePostUseCase(repository = repository)
    }

    @Provides
    fun providesLikePostUseCase(repository: HomeRepository): LikePostUseCase {
        return LikePostUseCase(repository = repository)
    }

    @Provides
    fun providesUpdatePostUseCase(repository: HomeRepository) : UpdatePostUseCase {
        return UpdatePostUseCase(repository = repository)
    }

    @Provides
    fun providesRemovePostLikeUseCase(repository: HomeRepository): RemovePostLikeUseCase {
        return RemovePostLikeUseCase(repository = repository)
    }

    @Provides
    fun providesFormatNumberUseCase(@ApplicationContext context: Context) : FormatNumberUseCase {
        return FormatNumberUseCase(context = context)
    }

    @Provides
    fun providesAddReplyUseCase(repository: HomeRepository): AddReplyUseCase {
        return AddReplyUseCase(repository = repository)
    }

    @Provides
    fun providesGetPostUseCase(repository: HomeRepository): GetPostUseCase {
        return GetPostUseCase(repository = repository)
    }
}