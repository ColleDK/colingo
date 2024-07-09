package com.colledk.onboarding.ui

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.user.domain.model.User
import com.colledk.user.domain.usecase.CreateUserUseCase
import com.colledk.user.domain.usecase.LoginUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val createUserUseCase: CreateUserUseCase,
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _login: Channel<Unit> = Channel(Channel.BUFFERED)
    val login: Flow<Unit> = _login.receiveAsFlow()

    private val _goToProfileSetup: Channel<Unit> = Channel(Channel.BUFFERED)
    val goToProfileSetup: Flow<Unit> = _goToProfileSetup.receiveAsFlow()

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    fun isEmailValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(email, password).onSuccess {
                _login.trySend(Unit)
            }.onFailure {
                _error.trySend(it)
            }
        }
    }

    fun createUser(name: String, email: String, password: String) {
        viewModelScope.launch {
            createUserUseCase(
                email = email,
                password = password,
                user = User(name = name)
            ).onSuccess {
                _goToProfileSetup.trySend(Unit)
            }.onFailure {
                _error.trySend(it)
            }
        }
    }
}