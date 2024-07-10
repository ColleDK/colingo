package com.colledk.home.domain.repository

import com.colledk.home.domain.model.Post

interface HomeRepository {
    suspend fun createPost(post: Post): Result<Post>
}