package com.colledk.profile.ui

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.profile.ui.uistates.ProfileUiState
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.UpdateUserUseCase
import com.colledk.user.domain.usecase.UploadProfilePicUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val uploadProfilePicUseCase: UploadProfilePicUseCase,
    private val updateUserUseCase: UpdateUserUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<ProfileUiState> = MutableStateFlow(ProfileUiState.Loading)
    val uiState: StateFlow<ProfileUiState> = _uiState

    fun getUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess {
                _uiState.value = ProfileUiState.Data(currentUser = it)
            }
        }
    }

    fun addProfilePicture(uri: Uri?) {
        viewModelScope.launch {
            uri?.let {
                getCurrentUserUseCase().onSuccess { user ->
                    uploadProfilePicUseCase(uri = uri, userId = user.id).onSuccess { imageUrl ->
                        updateUserUseCase(user = user.copy(profilePictures = user.profilePictures.plus(imageUrl))).onSuccess {
                            getUser()
                        }
                    }
                }
            }
        }
    }
}