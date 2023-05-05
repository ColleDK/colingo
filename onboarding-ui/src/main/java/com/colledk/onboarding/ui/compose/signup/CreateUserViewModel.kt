package com.colledk.onboarding.ui.compose.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.onboarding.domain.usecase.CreateUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CreateUserViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateUserUiState())
    val uiState: StateFlow<CreateUserUiState> = _uiState

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    fun updateEmail(email: String) {
        updateUiState(
            email = email
        )
    }

    fun updatePassword(password: String) {
        updateUiState(password = password)
    }

    fun updateRepeatPassword(password: String) {
        updateUiState(repeatPassword = password)
    }

    fun createUser(
        email: String,
        password: String,
        repeatPassword: String
    ) {
        viewModelScope.launch {
            updateUiState(showLoading = true)

            if (
                checkInput(
                    email = email,
                    password = password,
                    repeatPassword = repeatPassword
                )
            ) {
                createUserUseCase(
                    email = email,
                    password = password
                ).onSuccess {
                    Timber.d("User created successfully")
                    updateUiState(showLoading = false)
                }.onFailure {
                    _error.send(it).also {
                        updateUiState(showLoading = false)
                    }
                }
            } else {
                _error.send(Throwable()).also {
                    updateUiState(showLoading = false)
                }
            }
        }
    }

    private fun updateUiState(
        email: String? = null,
        password: String? = null,
        repeatPassword: String? = null,
        showLoading: Boolean? = null
    ) {
        val currentState = _uiState.value
        val newMail = email ?: currentState.email
        val newPass = password ?: currentState.password
        val newRepeatPass = repeatPassword ?: currentState.repeatPassword

        _uiState.value = currentState.copy(
            email = newMail,
            password = newPass,
            repeatPassword = newRepeatPass,
            showLoading = showLoading ?: currentState.showLoading,
            isButtonEnabled = checkInput(
                email = newMail,
                password = newPass,
                repeatPassword = newRepeatPass
            )
        )
    }

    private fun checkInput(
        email: String,
        password: String,
        repeatPassword: String
    ): Boolean {
        if (password.isEmpty() || repeatPassword.isEmpty()) {
            return false
        }

        return checkEmail(email = email) && password.equals(repeatPassword, ignoreCase = false)
    }

    private fun checkEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}