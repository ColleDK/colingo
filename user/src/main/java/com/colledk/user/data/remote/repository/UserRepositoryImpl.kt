package com.colledk.user.data.remote.repository

import android.app.GrammaticalInflectionManager
import android.content.res.Configuration
import android.net.Uri
import android.os.Build
import com.colledk.common.LocaleHelper
import com.colledk.user.data.mapToDomain
import com.colledk.user.data.mapToRemote
import com.colledk.user.data.remote.UserRemoteDataSource
import com.colledk.user.domain.LocationHelper
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okio.IOException

class UserRepositoryImpl(
    private val remoteDataSource: UserRemoteDataSource,
    private val locationHelper: LocationHelper,
    private val localeHelper: LocaleHelper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
): UserRepository {
    override suspend fun uploadProfilePicture(uri: Uri, userId: String): Result<Uri> = withContext(dispatcher) {
        remoteDataSource.uploadFile(file = uri.toString(), userId = userId).map { Uri.parse(it) }
    }

    override suspend fun addProfilePicture(picture: Uri): Result<User> = withContext(dispatcher) {
        remoteDataSource.addProfilePicture(picture.toString()).map { it.mapToDomain(locationHelper.getGeocoder()) }
    }

    override suspend fun deleteProfilePicture(picture: Uri): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.removeFile(path = picture.toString())
    }

    override suspend fun loginUser(email: String, password: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.loginUser(email, password).map {
            it.mapToDomain(locationHelper.getGeocoder()).also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    val gender = when(it.gender) {
                        Gender.MALE -> Configuration.GRAMMATICAL_GENDER_MASCULINE
                        Gender.FEMALE -> Configuration.GRAMMATICAL_GENDER_FEMININE
                        Gender.OTHER -> Configuration.GRAMMATICAL_GENDER_NEUTRAL
                    }

//                    localeHelper.updateLocaleGender(gender)
                }
            }
        }
    }

    override suspend fun getUser(userId: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.getUser(userId).map { it.mapToDomain(locationHelper.getGeocoder()) }
    }

    override suspend fun createUser(email: String, password: String, user: User): Result<User> = withContext(dispatcher) {
        remoteDataSource.createUser(
            email = email,
            password = password,
            user = user.mapToRemote()
        ).map { it.mapToDomain(locationHelper.getGeocoder()) }
    }

    override suspend fun updateUser(user: User): Result<User> = withContext(dispatcher) {
        remoteDataSource.updateUser(user.mapToRemote()).map {
            it.mapToDomain(locationHelper.getGeocoder()).also {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                    val gender = when(it.gender) {
                        Gender.MALE -> Configuration.GRAMMATICAL_GENDER_MASCULINE
                        Gender.FEMALE -> Configuration.GRAMMATICAL_GENDER_FEMININE
                        Gender.OTHER -> Configuration.GRAMMATICAL_GENDER_NEUTRAL
                    }

//                    localeHelper.updateLocaleGender(gender)
                }
            }
        }
    }

    override suspend fun addAiChat(userId: String, chatId: String): Result<User> = withContext(dispatcher) {
        val user = getUser(userId = userId).getOrNull()
        if (user == null) {
            Result.failure(IOException())
        } else {
            remoteDataSource.addAiChat(chatId = chatId).map { it.mapToDomain(locationHelper.getGeocoder()) }
        }
    }

    override suspend fun addChat(userId: String, chatId: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.addChat(userId, chatId).map { it.mapToDomain(locationHelper.getGeocoder()) }
    }

    override suspend fun deleteAiChat(userId: String, chatId: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.removeAiChat(userId = userId, chatId = chatId).map { it.mapToDomain(locationHelper.getGeocoder()) }
    }

    override suspend fun deleteChat(userId: String, chatId: String): Result<User> = withContext(dispatcher) {
        remoteDataSource.removeChat(userId = userId, chatId = chatId).map { it.mapToDomain(locationHelper.getGeocoder()) }
    }

    override suspend fun deleteUser(): Result<Unit> = withContext(dispatcher) {
        remoteDataSource.deleteUser()
    }
}