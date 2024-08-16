package com.colledk.user.domain.repository

import android.net.Uri
import com.colledk.user.domain.model.User

interface UserRepository {
    suspend fun uploadProfilePicture(uri: Uri, userId: String): Result<Uri>
    suspend fun deleteProfilePicture(picture: Uri): Result<Unit>
    suspend fun addProfilePicture(picture: Uri): Result<User>
    suspend fun loginUser(email: String, password: String): Result<User>
    suspend fun getUser(userId: String): Result<User>
    suspend fun createUser(email: String, password: String, user: User): Result<User>
    suspend fun updateUser(user: User): Result<User>
    suspend fun addAiChat(userId: String, chatId: String): Result<User>
    suspend fun addChat(userId: String, chatId: String): Result<User>
    suspend fun deleteUser(): Result<Unit>
}