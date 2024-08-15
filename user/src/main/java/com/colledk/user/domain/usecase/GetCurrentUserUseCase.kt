package com.colledk.user.domain.usecase

import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository
import com.google.firebase.auth.FirebaseAuth
import okio.IOException

class GetCurrentUserUseCase(
    private val repository: UserRepository,
    private val auth: FirebaseAuth
) {
    suspend operator fun invoke(): Result<User> {
        return auth.currentUser?.uid?.let {
            repository.getUser(it)
        } ?: run {
            Result.failure(IOException())
        }
    }
}