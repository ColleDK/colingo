package com.colledk.home.domain.usecase

import com.colledk.home.domain.model.Post
import com.colledk.home.domain.repository.HomeRepository

class UpdatePostUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(post: Post): Result<Unit> {
        return repository.updatePost(post = post)
    }
}