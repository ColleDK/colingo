package com.colledk.home.domain.usecase

import com.colledk.home.domain.model.Reply
import com.colledk.home.domain.repository.HomeRepository

class AddReplyUseCase(private val repository: HomeRepository) {
    suspend operator fun invoke(postId: String, reply: Reply): Result<Unit> {
        return repository.addReply(postId = postId, reply = reply)
    }
}