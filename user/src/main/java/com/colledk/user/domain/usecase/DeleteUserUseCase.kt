package com.colledk.user.domain.usecase

import com.colledk.user.domain.repository.UserRepository

class DeleteUserUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Result<Unit> {
        return repository.deleteUser()
    }
}