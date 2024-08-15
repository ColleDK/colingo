package com.colledk.user.data.remote.repository

import android.net.Uri
import com.colledk.user.data.mapToDomain
import com.colledk.user.data.mapToRemote
import com.colledk.user.data.remote.UserRemoteDataSource
import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException
import timber.log.Timber

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): UserRepository {
    override suspend fun uploadProfilePicture(uri: Uri, userId: String): Result<Uri> = withContext(dispatcher) {
        remoteDataSource.uploadFile(file = uri.toString(), userId = userId).map { Uri.parse(it) }
    }

    override suspend fun addProfilePicture(picture: Uri): Result<User> = withContext(dispatcher) {
        remoteDataSource.addProfilePicture(picture.toString()).map { it.mapToDomain() }
    }

    override suspend fun deleteProfilePicture(picture: Uri): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.removeFile(path = picture.toString())
    }

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

    override suspend fun addAiChat(userId: String, chatId: String): Result<User> = withContext(dispatcher) {
        val user = getUser(userId = userId).getOrNull()
        if (user == null) {
            Result.failure(IOException())
        } else {
            remoteDataSource.addAiChat(chatId = chatId).map { it.mapToDomain() }
        }
    }

    override suspend fun deleteUser(): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.deleteUser()
    }
}