package com.colledk.user.data.remote

import com.colledk.user.data.remote.model.UserRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import okio.IOException

class UserRemoteDataSource(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
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

    suspend fun createUser(user: UserRemote): Result<UserRemote> {
        try {
            val userId = db.collection(USERS_PATH).add(user).await().id
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

    companion object {
        const val USERS_PATH = "users"
    }
}