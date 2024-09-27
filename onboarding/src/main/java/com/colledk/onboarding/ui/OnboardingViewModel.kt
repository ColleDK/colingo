package com.colledk.onboarding.ui

import androidx.core.util.PatternsCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.common.MessageHandler
import com.colledk.user.domain.model.User
import com.colledk.user.domain.usecase.CreateUserUseCase
import com.colledk.user.domain.usecase.LoginUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val auth: FirebaseAuth,
    private val createUserUseCase: CreateUserUseCase,
    private val loginUseCase: LoginUseCase,
    private val errorHandler: MessageHandler
) : ViewModel() {

    private val _login: Channel<Long> = Channel(Channel.BUFFERED)
    val login: Flow<Long> = _login.receiveAsFlow()

    private val _goToProfileSetup: Channel<Long> = Channel(Channel.BUFFERED)
    val goToProfileSetup: Flow<Long> = _goToProfileSetup.receiveAsFlow()

    private val _resetPasswordSuccess: Channel<Long> = Channel(Channel.BUFFERED)
    val resetPasswordSuccess: Flow<Long> = _resetPasswordSuccess.receiveAsFlow()

    private val _isLoggingIn: Channel<Boolean> = Channel(Channel.BUFFERED)
    val isLoggingIn: Flow<Boolean> = _isLoggingIn.receiveAsFlow()

    fun isEmailValid(email: String): Boolean {
        return PatternsCompat.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoggingIn.trySend(true)
            loginUseCase(email, password).onSuccess {
                _login.trySend(DateTime.now().millis)
            }.onFailure {
                _isLoggingIn.trySend(false)
                errorHandler.displayError(it)
            }
        }
    }

    fun forgotPassword(email: String) {
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                _resetPasswordSuccess.trySend(DateTime.now().millis)
            } catch (e: Exception) {
                errorHandler.displayError(e)
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
                _goToProfileSetup.trySend(DateTime.now().millis)
            }.onFailure {
                errorHandler.displayError(it)
            }
        }
    }
}