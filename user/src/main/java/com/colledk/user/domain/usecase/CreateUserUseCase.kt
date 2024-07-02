package com.colledk.user.domain.usecase

import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository

class CreateUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(email: String, password: String, user: User): Result<User> {
        return repository.createUser(email = email, password = password, user = user)
    }
}