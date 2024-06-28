package com.colledk.chat.di

import com.colledk.chat.data.remote.ChatRemoteDataSource
import com.colledk.chat.data.remote.repository.ChatRepositoryImpl
import com.colledk.chat.domain.repository.ChatRepository
import com.colledk.chat.domain.usecase.GetChatUseCase
import com.colledk.chat.domain.usecase.GetChatsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ChatModule {

    @Provides
    fun providesChatRemoteDataSource(): ChatRemoteDataSource {
        return ChatRemoteDataSource()
    }

    @Provides
    @Singleton
    fun providesChatRepository(
        remoteDataSource: ChatRemoteDataSource
    ): ChatRepository {
        return ChatRepositoryImpl(remoteDataSource = remoteDataSource)
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
}