package com.colledk.home.data.remote.repository

import com.colledk.home.data.mapToDomain
import com.colledk.home.data.mapToRemote
import com.colledk.home.data.remote.HomeRemoteDataSource
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.repository.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val remoteDataSource: HomeRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeRepository {
    override suspend fun createPost(post: Post): Result<Post> = withContext(dispatcher) {
        remoteDataSource.createPost(post = post.mapToRemote()).map { it.mapToDomain(users = listOf(post.user)) }
    }
}