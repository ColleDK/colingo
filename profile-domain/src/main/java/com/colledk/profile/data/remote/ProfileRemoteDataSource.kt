package com.colledk.profile.data.remote

import com.colledk.profile.data.remote.model.ProfileRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.IOException

class ProfileRemoteDataSource(

) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun createProfile(profile: ProfileRemote): Result<Unit> {
        return when(val user = auth.currentUser) {
            null -> {
                Result.failure(IOException())
            }
            else -> {
                try {
                    if (db.collection(USERS_COLLECTION).document(user.uid).get().await().exists()) {
                        Result.failure(IOException())
                    } else {
                        db.collection(USERS_COLLECTION).document(user.uid).set(profile).await()

                        Result.success(Unit)
                    }
                } catch (e: Exception) {
                    Result.failure(exception = e)
                }
            }
        }
    }

    suspend fun getProfile(): Result<ProfileRemote?> {
        return when (val user = auth.currentUser) {
            null -> {
                Result.failure(IOException())
            }

            else -> {
                try {
                    val profile = db
                        .collection(USERS_COLLECTION)
                        .document(user.uid)
                        .get()
                        .await()
                        .toObject(ProfileRemote::class.java)

                    Result.success(profile)
                } catch (e: Exception) {
                    Result.failure(exception = e)
                }
            }
        }
    }

    suspend fun updateProfile(newProfile: ProfileRemote): Result<Unit> {
        return when (val user = auth.currentUser) {
            null -> {
                Result.failure(IOException())
            }

            else -> {
                try {
                    if (db.collection(USERS_COLLECTION).document(user.uid).get().await().exists()) {
                        db.collection(USERS_COLLECTION).document(user.uid).set(newProfile).await()

                        Result.success(Unit)
                    } else {
                        Result.failure(IOException())
                    }
                } catch (e: Exception) {
                    Result.failure(exception = e)
                }
            }
        }
    }

    companion object {
        const val USERS_COLLECTION = "users"
    }
}