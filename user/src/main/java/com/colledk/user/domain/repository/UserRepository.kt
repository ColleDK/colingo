package com.colledk.user.domain.repository

import com.colledk.user.domain.model.User

interface UserRepository {
    suspend fun loginUser(email: String, password: String): Result<User>
    suspend fun getUser(userId: String): Result<User>
    suspend fun createUser(email: String, password: String, user: User): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun deleteUser(): Result<Unit>
}