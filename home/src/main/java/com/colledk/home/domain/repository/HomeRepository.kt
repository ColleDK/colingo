package com.colledk.home.domain.repository

import com.colledk.home.domain.model.Post
import com.colledk.home.domain.model.Reply

interface HomeRepository {
    suspend fun getPost(postId: String): Result<Post>
    suspend fun createPost(post: Post): Result<Post>
    suspend fun updatePost(post: Post): Result<Unit>
    suspend fun likePost(postId: String, userId: String): Result<Unit>
    suspend fun removePostLike(postId: String, userId: String): Result<Unit>
    suspend fun addReply(postId: String, reply: Reply): Result<Unit>
}