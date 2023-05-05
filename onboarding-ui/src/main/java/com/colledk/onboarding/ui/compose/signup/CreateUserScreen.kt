package com.colledk.onboarding.ui.compose.signup

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.colledk.onboarding.ui.R

@Composable
internal fun CreateUserScreen(
    viewModel: CreateUserViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val localFocusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                paddingValues = PaddingValues(
                    horizontal = 36.dp
                )
            )
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CreateUserInputField(
            value = uiState.email,
            onValueChange = viewModel::updateEmail,
            labelName = R.string.onboarding_create_user_email_label,
            icon = Icons.Filled.Email,
            iconDescription = R.string.onboarding_create_user_email_icon_desc,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        CreateUserInputField(
            value = uiState.password,
            labelName = R.string.onboarding_create_user_password_label,
            onValueChange = viewModel::updatePassword,
            icon = Icons.Filled.Lock,
            iconDescription = R.string.onboarding_create_user_password_icon_desc,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onNext = {
                    localFocusManager.moveFocus(FocusDirection.Down)
                }
            )
        )

        CreateUserInputField(
            value = uiState.repeatPassword,
            labelName = R.string.onboarding_create_user_repeat_password_label,
            onValueChange = viewModel::updateRepeatPassword,
            icon = Icons.Filled.Lock,
            iconDescription = R.string.onboarding_create_user_password_icon_desc,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    localFocusManager.clearFocus()
                    viewModel.createUser(
                        email = uiState.email,
                        password = uiState.password,
                        repeatPassword = uiState.repeatPassword
                    )
                }
            ),
            isError = uiState.password.length != uiState.repeatPassword.length
        )

        CreateUserSignUpButton(
            onClick = {
                viewModel.createUser(
                    email = uiState.email,
                    password = uiState.password,
                    repeatPassword = uiState.repeatPassword
                )
            },
            enabled = uiState.isButtonEnabled
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateUserInputField(
    `value`: String,
    @StringRes labelName: Int,
    icon: ImageVector,
    @StringRes iconDescription: Int,
    onValueChange: (String) -> Unit,
    keyboardOptions: KeyboardOptions = KeyboardOptions(),
    keyboardActions: KeyboardActions = KeyboardActions(),
    isError: Boolean = false
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(text = stringResource(id = labelName))
        },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = stringResource(id = iconDescription)
            )
        },
        keyboardActions = keyboardActions,
        keyboardOptions = keyboardOptions,
        isError = isError
    )
}

@Composable
private fun CreateUserSignUpButton(
    onClick: () -> Unit,
    enabled: Boolean
) {
    val btnLabel = stringResource(id = R.string.onboarding_create_user_btn_action_label)

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                paddingValues = PaddingValues(
                    horizontal = 24.dp
                )
            )
            .clickable(
                enabled = enabled,
                onClick = onClick,
                onClickLabel = btnLabel
            ),
        enabled = enabled
    ) {
        Text(
            text = stringResource(id = R.string.onboarding_create_user_btn_label),
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Preview(
    name = "Extra large font",
    group = "Font group",
    fontScale = 2.5f
)
@Preview(
    name = "Large font",
    group = "Font group",
    fontScale = 1.5f
)
@Preview(
    name = "Normal font",
    group = "Font group",
    fontScale = 1f
)
@Composable
fun CreateUserScreenPreview(

) {
    Surface(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        CreateUserScreen()
    }
}