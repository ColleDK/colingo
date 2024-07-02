package com.colledk.user.domain.usecase

import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository

class LoginUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.loginUser(email = email, password = password)
    }
}