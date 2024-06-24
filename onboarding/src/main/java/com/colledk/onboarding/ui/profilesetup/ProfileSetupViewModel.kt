package com.colledk.onboarding.ui.profilesetup

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.colledk.onboarding.domain.Gender
import com.colledk.onboarding.domain.Topic
import com.colledk.onboarding.domain.UserLanguage
import com.colledk.onboarding.ui.profilesetup.uistates.DescriptionUiState
import com.colledk.onboarding.ui.profilesetup.uistates.GenderUiState
import com.colledk.onboarding.ui.profilesetup.uistates.LanguagesUiState
import com.colledk.onboarding.ui.profilesetup.uistates.ProfilePictureUiState
import com.colledk.onboarding.ui.profilesetup.uistates.TopicsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ProfileSetupViewModel @Inject constructor(

) : ViewModel() {
    private val _profilePictureState: MutableStateFlow<ProfilePictureUiState> = MutableStateFlow(ProfilePictureUiState())
    val profilePictureState: StateFlow<ProfilePictureUiState> = _profilePictureState

    private val _descriptionState: MutableStateFlow<DescriptionUiState> = MutableStateFlow(DescriptionUiState())
    val descriptionState: StateFlow<DescriptionUiState> = _descriptionState

    private val _languagesState: MutableStateFlow<LanguagesUiState> = MutableStateFlow(LanguagesUiState())
    val languagesState: StateFlow<LanguagesUiState> = _languagesState

    private val _topicState: MutableStateFlow<TopicsUiState> = MutableStateFlow(TopicsUiState())
    val topicState: StateFlow<TopicsUiState> = _topicState

    private val _genderState: MutableStateFlow<GenderUiState> = MutableStateFlow(GenderUiState())
    val genderState: StateFlow<GenderUiState> = _genderState

    fun updateProfilePicture(uri: Uri?) {
        _profilePictureState.value = _profilePictureState.value.copy(profilePicture = uri)
    }

    fun removeProfilePicture() {
        _profilePictureState.value = _profilePictureState.value.copy(profilePicture = null)
    }

    fun updateBirthday(dateTime: Long) {
        _descriptionState.value = _descriptionState.value.copy(birthday = dateTime.toLocalDate())
    }

    fun getLocation() {
        // TODO
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