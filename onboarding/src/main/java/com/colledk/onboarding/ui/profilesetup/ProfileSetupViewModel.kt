package com.colledk.onboarding.ui.profilesetup

import android.annotation.SuppressLint
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.onboarding.ui.profilesetup.uistates.DescriptionUiState
import com.colledk.onboarding.ui.profilesetup.uistates.GenderUiState
import com.colledk.onboarding.ui.profilesetup.uistates.LanguagesUiState
import com.colledk.onboarding.ui.profilesetup.uistates.ProfilePictureUiState
import com.colledk.onboarding.ui.profilesetup.uistates.TopicsUiState
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.Location
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.UserLanguage
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.GetUserUseCase
import com.colledk.user.domain.usecase.UpdateUserUseCase
import com.colledk.user.domain.usecase.UploadProfilePicUseCase
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(
    private val locationProviderClient: FusedLocationProviderClient,
    private val geocoder: Geocoder,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val uploadProfilePicUseCase: UploadProfilePicUseCase
) : ViewModel() {
    private val _profilePictureState: MutableStateFlow<ProfilePictureUiState> =
        MutableStateFlow(ProfilePictureUiState())
    val profilePictureState: StateFlow<ProfilePictureUiState> = _profilePictureState

    private val _descriptionState: MutableStateFlow<DescriptionUiState> =
        MutableStateFlow(DescriptionUiState())
    val descriptionState: StateFlow<DescriptionUiState> = _descriptionState

    private val _languagesState: MutableStateFlow<LanguagesUiState> =
        MutableStateFlow(LanguagesUiState())
    val languagesState: StateFlow<LanguagesUiState> = _languagesState

    private val _topicState: MutableStateFlow<TopicsUiState> = MutableStateFlow(TopicsUiState())
    val topicState: StateFlow<TopicsUiState> = _topicState

    private val _genderState: MutableStateFlow<GenderUiState> = MutableStateFlow(GenderUiState())
    val genderState: StateFlow<GenderUiState> = _genderState

    private val _goToFrontpage: Channel<Unit> = Channel(Channel.BUFFERED)
    val goToFrontpage: Flow<Unit> = _goToFrontpage.receiveAsFlow()

    fun finishSetup() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { user ->
                val description = _descriptionState.value
                val picture = _profilePictureState.value
                val languages = _languagesState.value
                val gender = _genderState.value

                val profilePicture = picture.profilePicture?.let {
                    uploadProfilePicUseCase(uri = picture.profilePicture, userId = user.id).getOrNull()
                }

                val newUser = user.copy(
                    birthday = description.birthday ?: DateTime.now(),
                    profilePictures = listOfNotNull(profilePicture),
                    description = description.description,
                    location = description.location ?: Location(),
                    languages = languages.languages,
                    gender = gender.selectedGender ?: Gender.OTHER
                )
                updateUserUseCase(user = newUser).onSuccess {
                    _goToFrontpage.trySend(Unit)
                }
            }
        }
    }

    fun updateProfilePicture(uri: Uri?) {
        _profilePictureState.value = _profilePictureState.value.copy(profilePicture = uri)
    }

    fun removeProfilePicture() {
        _profilePictureState.value = _profilePictureState.value.copy(profilePicture = null)
    }

    fun updateBirthday(dateTime: Long) {
        _descriptionState.value = _descriptionState.value.copy(birthday = DateTime(dateTime))
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
                        _descriptionState.value = _descriptionState.value.copy(
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
                    _descriptionState.value =
                        _descriptionState.value.copy(
                            location = Location(
                                country = address.countryName,
                                city = address.locality
                            )
                        )
                }
            }
        }
    }

    fun updateDescription(description: String) {
        _descriptionState.value = _descriptionState.value.copy(description = description)
    }

    fun addLanguage(language: UserLanguage) {
        val currentState = _languagesState.value
        _languagesState.value = currentState.copy(
            languages = currentState.languages.plus(language)
        )
    }

    fun removeLanguage(language: UserLanguage) {
        val currentState = _languagesState.value
        _languagesState.value = currentState.copy(
            languages = currentState.languages.minus(language)
        )
    }

    fun selectTopic(topic: Topic) {
        val currentState = _topicState.value
        _topicState.value = currentState.copy(
            selectedTopics = currentState.selectedTopics.plus(topic)
        )
    }

    fun removeTopic(topic: Topic) {
        val currentState = _topicState.value
        _topicState.value = currentState.copy(
            selectedTopics = currentState.selectedTopics.minus(topic)
        )
    }

    fun selectGender(gender: Gender) {
        _genderState.value = _genderState.value.copy(selectedGender = gender)
    }
}