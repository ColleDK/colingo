package com.colledk.onboarding.data.remote

import com.colledk.onboarding.data.mapper.toCountryRemote
import com.colledk.onboarding.data.remote.model.CountryRemote
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class OnboardingRemoteDataSource(
    private val db: FirebaseFirestore = Firebase.firestore
) {

    suspend fun getCountries(): Result<List<CountryRemote>> {
        return try {
            val countries = db.collection(COUNTRIES_COLLECTION)
                .get()
                .await()
                .documents
                .mapNotNull {
                    it.data?.toCountryRemote()
                }
            Result.success(countries)
        } catch (e: Exception) {
            Result.failure(exception = e)
        }
    }


    companion object {
        const val COUNTRIES_COLLECTION = "countries"
    }
}