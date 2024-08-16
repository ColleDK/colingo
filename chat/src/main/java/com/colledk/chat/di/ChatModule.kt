package com.colledk.chat.di

import com.colledk.chat.data.remote.ChatRemoteDataSource
import com.colledk.chat.data.remote.repository.ChatRepositoryImpl
import com.colledk.chat.domain.repository.ChatRepository
import com.colledk.chat.domain.usecase.CreateAiChatUseCase
import com.colledk.chat.domain.usecase.CreateChatUseCase
import com.colledk.chat.domain.usecase.GetAiChatUseCase
import com.colledk.chat.domain.usecase.GetAiChatsUseCase
import com.colledk.chat.domain.usecase.GetChatUseCase
import com.colledk.chat.domain.usecase.GetChatsUseCase
import com.colledk.chat.domain.usecase.SendMessageUseCase
import com.colledk.chat.domain.usecase.UpdateAiChatUseCase
import com.colledk.user.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {

    @Provides
    fun providesChatRemoteDataSource(
        db: FirebaseFirestore,
        auth: FirebaseAuth
    ): ChatRemoteDataSource {
        return ChatRemoteDataSource(
            db = db,
            auth = auth
        )
    }

    @Provides
    @Singleton
    fun providesChatRepository(
        remoteDataSource: ChatRemoteDataSource,
        userRepository: UserRepository,
        auth: FirebaseAuth
    ): ChatRepository {
        return ChatRepositoryImpl(
            remoteDataSource = remoteDataSource,
            userRepository = userRepository,
            auth = auth
        )
    }

    @Provides
    fun providesSendMessageUseCase(
        repository: ChatRepository
    ): SendMessageUseCase {
        return SendMessageUseCase(repository = repository)
    }

    @Provides
    fun providesGetChatUseCase(
        repository: ChatRepository
    ): GetChatUseCase {
        return GetChatUseCase(repository = repository)
    }

    @Provides
    fun providesGetChatsUseCase(
        repository: ChatRepository
    ): GetChatsUseCase {
        return GetChatsUseCase(repository = repository)
    }

    @Provides
    fun providesCreateAiChatUseCase(
        repository: ChatRepository
    ): CreateAiChatUseCase {
        return CreateAiChatUseCase(repository = repository)
    }

    @Provides
    fun providesGetAiChatsUseCase(
        repository: ChatRepository
    ): GetAiChatsUseCase {
        return GetAiChatsUseCase(repository = repository)
    }

    @Provides
    fun providesGetAiChatUseCase(
        repository: ChatRepository
    ): GetAiChatUseCase {
        return GetAiChatUseCase(repository = repository)
    }

    @Provides
    fun providesUpdateAiChatUseCase(
        repository: ChatRepository
    ): UpdateAiChatUseCase {
        return UpdateAiChatUseCase(repository = repository)
    }

    @Provides
    fun providesCreateChatUseCase(
        repository: ChatRepository
    ): CreateChatUseCase {
        return CreateChatUseCase(repository = repository)
    }
}