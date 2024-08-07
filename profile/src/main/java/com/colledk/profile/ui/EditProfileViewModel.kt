package com.colledk.profile.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.profile.ui.uistates.EditProfileUiState
import com.colledk.user.domain.model.User
import com.colledk.user.domain.usecase.AddProfilePictureUseCase
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.UploadProfilePicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val uploadProfilePicUseCase: UploadProfilePicUseCase,
    private val addProfilePictureUseCase: AddProfilePictureUseCase
) : ViewModel() {
    private val _currentState: MutableStateFlow<EditProfileUiState> = MutableStateFlow(EditProfileUiState(User()))
    val currentState: StateFlow<EditProfileUiState> = _currentState

    fun setUser(user: User) {
        _currentState.value = EditProfileUiState(user = user)
    }

    fun updateName(name: String) {
        updateUser(name = name)
    }

    fun saveUser() {
        // TODO
    }

    fun addProfilePicture(uri: Uri) {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess { user ->
                uploadProfilePicUseCase(uri = uri, userId = user.id).onSuccess { imageUrl ->
                    addProfilePictureUseCase(uri = imageUrl).onSuccess {
//                        _currentState.value = it
                    }
                }
            }
        }
    }

    private fun updateUser(
        name: String? = null,
        pictures: List<Uri>? = null
    ) {
        val currentUser = _currentState.value.user
        _currentState.value = EditProfileUiState(
            currentUser.copy(
                name = name ?: currentUser.name,
                profilePictures = pictures ?: currentUser.profilePictures
            )
        )
    }
}