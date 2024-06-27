package com.colledk.user.domain.usecase

import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository

class GetUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String): Result<User> {
        return repository.getUser(userId = userId)
    }
}