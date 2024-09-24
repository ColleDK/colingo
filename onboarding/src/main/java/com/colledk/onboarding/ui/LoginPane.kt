package com.colledk.onboarding.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colledk.onboarding.R
import com.colledk.theme.ColingoTheme
import com.colledk.theme.PreviewAnnotations
import timber.log.Timber

@Composable
internal fun LoginPane(
    isLoginButtonEnabled: Boolean,
    onForgotPassword: (email: String) -> Unit,
    onLogin: (email: String, password: String) -> Unit,
    onGoToSignup: () -> Unit,
    isEmailValid: (email: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var showForgotPassword by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(key1 = showForgotPassword) {
        Timber.d("New forgot password $showForgotPassword")
    }

    if (showForgotPassword) {
        ForgotPasswordDialog(
            onReset = { onForgotPassword(it).also { showForgotPassword = false } },
            onDismiss = { showForgotPassword = false },
            isEmailValid = isEmailValid
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FillOrMinHeight(minHeight = 24.dp)
        Image(
            painter = painterResource(id = R.drawable.world),
            modifier = Modifier.fillMaxWidth(),
            contentDescription = null // Not needed for talkback
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.login_title),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.login_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(24.dp))
        LoginInputField(
            value = email,
            labelTextId = R.string.login_email_hint,
            iconId = R.drawable.email,
            isPassword = false,
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            email = it
        }
        Spacer(modifier = Modifier.height(12.dp))
        LoginInputField(
            value = password,
            labelTextId = R.string.login_password_hint,
            iconId = R.drawable.password,
            isPassword = true,
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            password = it
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(id = R.string.login_forgot_password),
            style = MaterialTheme.typography.labelMedium,
            modifier = Modifier
                .fillMaxWidth(.8f)
                .clickable {
                    showForgotPassword = true
                },
            textAlign = TextAlign.End,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(20.dp))
        LoginButton(
            isEnabled = !isLoginButtonEnabled && password.isNotBlank() && email.isNotBlank() && isEmailValid(email),
            modifier = Modifier
                .fillMaxWidth(.5f)
                .heightIn(min = 48.dp)
        ) {
            onLogin(email, password)
        }
        FillOrMinHeight(minHeight = 24.dp)
        Row(
            modifier = Modifier.clickable {
                onGoToSignup()
            },
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.login_go_to_signup_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.login_go_to_signup_2),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textDecoration = TextDecoration.Underline,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

/**
 * Workaround to fill entire space on larger screens,
 * but having minimum height on smaller screens or when keyboard is active.
 */
@Composable
private fun ColumnScope.FillOrMinHeight(minHeight: Dp) {
    Spacer(modifier = Modifier.weight(1f))
    Spacer(modifier = Modifier.height(minHeight))
}

@Composable
private fun LoginInputField(
    value: String,
    @StringRes labelTextId: Int,
    @DrawableRes iconId: Int,
    isPassword: Boolean,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    var showPassword by remember {
        mutableStateOf(false)
    }

    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = stringResource(id = labelTextId),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        trailingIcon = {
            if (isPassword && value.isNotEmpty()) {
                Icon(
                    painter = painterResource(id = if (showPassword) R.drawable.password_shown else R.drawable.password_hidden),
                    contentDescription = null, // TODO
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable {
                            showPassword = !showPassword
                        }
                )
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Email),
        visualTransformation = if (isPassword && !showPassword) PasswordVisualTransformation() else VisualTransformation.None,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier,
        singleLine = true
    )
}

@Composable
private fun LoginButton(
    isEnabled: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = isEnabled,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Text(
            text = stringResource(id = R.string.login_login_btn),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ForgotPasswordDialog(
    isEmailValid: (email: String) -> Boolean,
    onReset: (email: String) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember {
        mutableStateOf("")
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Text(text = stringResource(id = R.string.login_reset_password))
        },
        text = {
            TextField(
                value = email,
                onValueChange = { email = it },
                label = {
                    Text(text = stringResource(id = R.string.login_email_hint))
                }
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.add_language_cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onReset(email)
                },
                enabled = email.isNotBlank() && isEmailValid(email)
            ) {
                Text(text = stringResource(id = R.string.add_language_select))
            }
        }
    )
}

@PreviewAnnotations
@Composable
private fun LoginPanePreview() {
    ColingoTheme {
        Surface {
            LoginPane(
                onForgotPassword = {  },
                onLogin = {_ ,_ -> },
                onGoToSignup = {  },
                isEmailValid = { true },
                isLoginButtonEnabled = true
            )
        }
    }
}