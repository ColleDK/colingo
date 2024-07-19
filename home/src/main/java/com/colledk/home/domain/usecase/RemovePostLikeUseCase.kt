package com.colledk.home.domain.usecase

import com.colledk.home.domain.model.Post
import com.colledk.home.domain.repository.HomeRepository

class RemovePostLikeUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(postId: String, userId: String): Result<Unit> {
        return repository.removePostLike(postId = postId, userId = userId)
    }
}