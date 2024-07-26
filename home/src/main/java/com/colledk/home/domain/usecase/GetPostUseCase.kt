package com.colledk.home.domain.usecase

import com.colledk.home.domain.model.Post
import com.colledk.home.domain.repository.HomeRepository

class GetPostUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(postId: String): Result<Post> {
        return repository.getPost(postId = postId)
    }
}