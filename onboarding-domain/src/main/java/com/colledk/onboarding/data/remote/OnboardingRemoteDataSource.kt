package com.colledk.onboarding.data.remote

import com.colledk.country.data.remote.model.CountryRemote
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import java.io.IOException

class OnboardingRemoteDataSource(

) {
    private val db: FirebaseFirestore = Firebase.firestore
    private val auth: FirebaseAuth = Firebase.auth

    suspend fun getCountries(): Result<List<CountryRemote>> {
        return try {
            val countries = db.collection(COUNTRIES_COLLECTION)
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.toObject(CountryRemote::class.java)
                }

            Result.success(countries)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }

    suspend fun createUser(
        email: String,
        password: String
    ): Result<Unit> {
        return try {
            auth.createUserWithEmailAndPassword(
                email, password
            ).await().user?.let {
                Result.success(Unit)
            } ?: run {
                Result.failure(IOException())
            }
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }


    companion object {
        const val COUNTRIES_COLLECTION = "countries"
    }
}