package com.colledk.user.data.remote

import android.net.Uri
import com.colledk.user.data.remote.model.UserRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import okio.IOException
import java.util.UUID

class UserRemoteDataSource(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage
) {
    suspend fun loginUser(email: String, password: String): Result<UserRemote> {
        try {
            val userId = auth.signInWithEmailAndPassword(email, password).await().user?.uid
            return if (userId == null) {
                Result.failure(IOException())
            } else {
                getUser(userId = userId)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun getUser(userId: String): Result<UserRemote> {
        try {
            val user = db
                .collection(USERS_PATH)
                .document(userId)
                .get()
                .await()
                .toObject(UserRemote::class.java)

            return if (user == null) {
                Result.failure(IOException())
            } else {
                Result.success(value = user)
            }

        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun createUser(email: String, password: String, user: UserRemote): Result<UserRemote> {
        try {
            val userId = auth.createUserWithEmailAndPassword(email, password).await().user?.uid ?: return Result.failure(IOException())
            val newUser = user.copy(id = userId)
            db.collection(USERS_PATH).document(userId).set(newUser).await()
            return getUser(userId = userId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updateUser(user: UserRemote): Result<UserRemote> {
        return when(val id = auth.currentUser?.uid) {
            null -> {
                Result.failure(IOException())
            }
            else -> {
                try {
                    if (db.collection(USERS_PATH).document(id).get().await().exists()) {
                        db.collection(USERS_PATH).document(id).set(user).await()

                        getUser(userId = id)
                    } else {
                        Result.failure(IOException())
                    }
                } catch (e: Exception) {
                    Result.failure(IOException())
                }
            }
        }
    }

    suspend fun addProfilePicture(profilePic: String): Result<UserRemote> {
        return when(val id = auth.currentUser?.uid) {
            null -> {
                Result.failure(IOException())
            }
            else -> {
                try {
                    if (db.collection(USERS_PATH).document(id).get().await().exists()) {
                        db.collection(USERS_PATH).document(id).update(PROFILE_PICTURE_PATH, FieldValue.arrayUnion(profilePic))

                        getUser(userId = id)
                    } else {
                        Result.failure(IOException())
                    }
                } catch (e: Exception) {
                    Result.failure(IOException())
                }
            }
        }
    }

    suspend fun addAiChat(chatId: String): Result<UserRemote> {
        return when(val id = auth.currentUser?.uid) {
            null -> {
                Result.failure(IOException())
            }
            else -> {
                try {
                    if (db.collection(USERS_PATH).document(id).get().await().exists()) {
                        db.collection(USERS_PATH).document(id).update(AI_CHAT_PATH, FieldValue.arrayUnion(chatId))

                        getUser(userId = id)
                    } else {
                        Result.failure(IOException())
                    }
                } catch (e: Exception) {
                    Result.failure(IOException())
                }
            }
        }
    }

    suspend fun addChat(userId: String, chatId: String): Result<UserRemote> {
        return try {
            if (db.collection(USERS_PATH).document(userId).get().await().exists()) {
                db.collection(USERS_PATH).document(userId).update(CHATS_PATH, FieldValue.arrayUnion(chatId))

                getUser(userId = userId)
            } else {
                Result.failure(IOException())
            }
        } catch (e: Exception) {
            Result.failure(IOException())
        }
    }

    suspend fun deleteUser(): Result<Unit> {
        return when(val id = auth.currentUser?.uid) {
            null -> {
                Result.failure(IOException())
            }
            else -> {
                try {
                    if (db.collection(USERS_PATH).document(id).get().await().exists()) {
                        db.collection(USERS_PATH).document(id).delete().await()

                        Result.success(Unit)
                    } else {
                        Result.failure(IOException())
                    }
                } catch (e: Exception) {
                    Result.failure(IOException())
                }
            }
        }
    }

//    suspend fun uploadFiles(images: List<String>, userId: String): Result<List<String>> {
//        try {
//            return images.map {
//                uploadFile(file = it, userId = userId)
//            }
//        } catch (e: Exception) {
//            return Result.failure(e)
//        }
//    }

    suspend fun uploadFile(file: String, userId: String): Result<String> {
        try {
            val ref = storage.reference.child(IMAGE_PATH + userId + "/" + UUID.randomUUID())
            ref.putFile(Uri.parse(file)).await()
            val url = ref.downloadUrl.await().toString()
            return Result.success(url)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun removeFile(path: String): Result<Unit> {
        try {
            storage.getReferenceFromUrl(path).delete().await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    companion object {
        const val USERS_PATH = "users"
        const val IMAGE_PATH = "images/"
        const val AI_CHAT_PATH = "aiChats"
        const val CHATS_PATH = "chats"
        const val PROFILE_PICTURE_PATH = "profilePictures"
    }
}