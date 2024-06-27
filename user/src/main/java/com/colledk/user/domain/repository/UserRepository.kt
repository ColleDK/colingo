package com.colledk.user.domain.repository

import com.colledk.user.domain.model.User

interface UserRepository {
    suspend fun getUser(userId: String): Result<User>
    suspend fun createUser(user: User): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(): Result<Unit>
}