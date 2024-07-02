package com.colledk.user.data.remote.repository

import com.colledk.user.data.mapToDomain
import com.colledk.user.data.mapToRemote
import com.colledk.user.data.remote.UserRemoteDataSource
import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): UserRepository {
    override suspend fun loginUser(email: String, password: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.loginUser(email, password).map { it.mapToDomain() }
    }

    override suspend fun getUser(userId: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.getUser(userId).map { it.mapToDomain() }
    }

    override suspend fun createUser(email: String, password: String, user: User): Result<User> = withContext(dispatcher) {
        remoteDataSource.createUser(
            email = email,
            password = password,
            user = user.mapToRemote()
        ).map { it.mapToDomain() }
    }

    override suspend fun updateUser(user: User): Result<User> = withContext(dispatcher) {
        remoteDataSource.updateUser(user.mapToRemote()).map { it.mapToDomain() }
    }

    override suspend fun deleteUser(): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.deleteUser()
    }
}