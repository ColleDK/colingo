package com.colledk.onboarding.ui.compose.signup

data class CreateUserUiState(
    val email: String = "",
    val password: String = "",
    val repeatPassword: String = "",
    val showLoading: Boolean = false,
    val isButtonEnabled: Boolean = false
)
