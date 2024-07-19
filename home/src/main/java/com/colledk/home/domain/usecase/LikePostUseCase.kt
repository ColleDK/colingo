package com.colledk.home.domain.usecase

import com.colledk.home.domain.model.Post
import com.colledk.home.domain.repository.HomeRepository

class LikePostUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(postId: String, userId: String): Result<Unit> {
        return repository.likePost(postId = postId, userId = userId)
    }
}