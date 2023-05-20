package com.colledk.onboarding.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.colledk.accessibility.BaseAccessibilityTest
import com.colledk.onboarding.ui.compose.signup.CreateUserScreen
import com.colledk.onboarding.ui.compose.signup.CreateUserUiState
import org.junit.Test

class CreateUserScreenUiTest: BaseAccessibilityTest() {
    
    @Test
    fun signUpAccessibilityTest() {
        composeTestRule.setContent { 
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                CreateUserScreen(
                    uiState = CreateUserUiState(),
                    updateEmail = {},
                    updatePassword = {},
                    updateRepeatPassword = {},
                    onCreateUser = {_, _, _ -> }
                )
            }
        }

        testAccessibility()
    }
}