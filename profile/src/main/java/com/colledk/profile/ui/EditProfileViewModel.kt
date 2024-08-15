package com.colledk.profile.ui

import android.annotation.SuppressLint
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.profile.ui.uistates.EditProfileUiState
import com.colledk.user.domain.model.Location
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.User
import com.colledk.user.domain.model.UserLanguage
import com.colledk.user.domain.usecase.DeleteProfilePictureUseCase
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.UpdateUserUseCase
import com.colledk.user.domain.usecase.UploadProfilePicUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import timber.log.Timber
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val locationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val uploadProfilePicUseCase: UploadProfilePicUseCase,
    private val deleteProfilePictureUseCase: DeleteProfilePictureUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _currentState: MutableStateFlow<EditProfileUiState> = MutableStateFlow(EditProfileUiState(User()))
    val currentState: StateFlow<EditProfileUiState> = _currentState

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    private val _refreshPage: Channel<String> = Channel(Channel.BUFFERED)
    val refreshPage: Flow<String> = _refreshPage.receiveAsFlow()

    fun setUser(user: User) {
        _currentState.value = EditProfileUiState(user = user)
    }

    fun updatePictures(pictures: List<Uri>) {
        updateUser(pictures = pictures)
    }

    fun updateName(name: String) {
        updateUser(name = name)
    }

    fun updateDescription(description: String) {
        updateUser(description = description)
    }

    fun updateLanguages(languages: List<UserLanguage>) {
        updateUser(languages = languages)
    }

    fun updateTopics(topics: List<Topic>) {
        updateUser(topics = topics)
    }

    fun updateBirthday(time: Long) {
        updateUser(birthday = DateTime(time))
    }

    // This is only called when permissions are enabled
    @SuppressLint("MissingPermission")
    fun getLocation() {
        viewModelScope.launch {
            val location = locationProviderClient.getCurrentLocation(
                Priority.PRIORITY_BALANCED_POWER_ACCURACY,
                CancellationTokenSource().token
            ).await()

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                ) {
                    it.firstOrNull()?.let { address ->
                        updateUser(
                            location = Location(
                                country = address.countryName,
                                city = address.locality
                            )
                        )
                    }
                }
            } else {
                geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )?.firstOrNull()?.let { address ->
                    updateUser(
                        location = Location(
                            country = address.countryName,
                            city = address.locality
                        )
                    )
                }
            }
        }
    }

    fun saveUser(newUser: User) {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { originalUser ->
                Timber.d("Current userId ${newUser.id}")
                val removedPictures = originalUser.profilePictures.filterNot { newUser.profilePictures.contains(it) }
                val addedPictures = newUser.profilePictures.filterNot { originalUser.profilePictures.contains(it) }

                // Remove all deleted pictures from storage
                removedPictures.map {
                    async { deleteProfilePictureUseCase(picture = it) }
                }.awaitAll()

                // Add all new pictures to storage
                val pictureUri = addedPictures.map {
                    async { uploadProfilePicUseCase(uri = it, userId = originalUser.id) }
                }.awaitAll().mapNotNull { it.getOrNull() }

                val updateUser = newUser.copy(profilePictures = newUser.profilePictures.filterNot { addedPictures.contains(it) }.plus(pictureUri))
                updateUserUseCase(user = updateUser).onSuccess {
                    _refreshPage.trySend(UUID.randomUUID().toString())
                }.onFailure {
                    _error.trySend(it)
                }
            }
        }
    }

    private fun updateUser(
        name: String? = null,
        birthday: DateTime? = null,
        location: Location? = null,
        topics: List<Topic>? = null,
        languages: List<UserLanguage>? = null,
        description: String? = null,
        pictures: List<Uri>? = null
    ) {
        val currentUser = _currentState.value.user
        _currentState.value = EditProfileUiState(
            currentUser.copy(
                name = name ?: currentUser.name,
                birthday = birthday ?: currentUser.birthday,
                location = location ?: currentUser.location,
                topics = topics ?: currentUser.topics,
                languages = languages ?: currentUser.languages,
                description = description ?: currentUser.description,
                profilePictures = pictures ?: currentUser.profilePictures
            )
        )
    }
}