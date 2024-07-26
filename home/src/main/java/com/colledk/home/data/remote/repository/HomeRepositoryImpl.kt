package com.colledk.home.data.remote.repository

import com.colledk.home.data.mapToDomain
import com.colledk.home.data.mapToRemote
import com.colledk.home.data.remote.HomeRemoteDataSource
import com.colledk.home.data.remote.model.PostRemote
import com.colledk.home.data.remote.model.ReplyRemote
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.model.Reply
import com.colledk.home.domain.repository.HomeRepository
import com.colledk.user.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HomeRepositoryImpl(
    private val userRepository: UserRepository,
    private val remoteDataSource: HomeRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : HomeRepository {
    override suspend fun getPost(postId: String): Result<Post> = withContext(dispatcher) {
        remoteDataSource.getPost(postId = postId).map {
            val users = getUserIds(listOf(it)).mapNotNull { userRepository.getUser(it).getOrNull() }
            it.mapToDomain(users)
        }
    }

    private fun getUserIds(posts: List<PostRemote>): List<String> {
        val postUser = posts.map { it.userId }
        val replyUser = posts.map { it.replies.map { getReplyUser(it) }.flatten() }.flatten()
        return (postUser + replyUser).distinct()
    }

    private fun getReplyUser(reply: ReplyRemote): List<String> {
        return listOf(reply.userId) + reply.replies.map { getReplyUser(it) }.flatten()
    }

    override suspend fun createPost(post: Post): Result<Post> = withContext(dispatcher) {
        remoteDataSource.createPost(post = post.mapToRemote()).map { it.mapToDomain(users = listOf(post.user)) }
    }

    override suspend fun updatePost(post: Post): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.updatePost(post.mapToRemote())
    }

    override suspend fun likePost(postId: String, userId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.likePost(postId = postId, userId = userId)
    }

    override suspend fun removePostLike(postId: String, userId: String): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.removePostLike(postId = postId, userId = userId)
    }

    override suspend fun addReply(postId: String, reply: Reply): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.addReply(postId = postId, reply = reply.mapToRemote())
    }
}