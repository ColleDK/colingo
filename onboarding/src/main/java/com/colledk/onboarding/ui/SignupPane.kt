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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.colledk.onboarding.R
import com.colledk.theme.ColingoTheme
import com.colledk.theme.PreviewAnnotations

@Composable
internal fun SignupPane(
    onRegister: (name: String, email: String, password: String) -> Unit,
    goToLogin: () -> Unit,
    isEmailValid: (email: String) -> Boolean,
    modifier: Modifier = Modifier
) {
    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
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
            contentDescription = null // Not needed for talkback
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.signup_title),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(id = R.string.signup_subtitle),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(24.dp))
        SignupInputField(
            value = name,
            labelTextId = R.string.signup_name_hint,
            iconId = R.drawable.user,
            isPassword = false,
            keyboardType = KeyboardType.Text,
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            name = it
        }
        Spacer(modifier = Modifier.height(12.dp))
        SignupInputField(
            value = email,
            labelTextId = R.string.signup_email_hint,
            iconId = R.drawable.email,
            isPassword = false,
            keyboardType = KeyboardType.Email,
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            email = it
        }
        Spacer(modifier = Modifier.height(12.dp))
        SignupInputField(
            value = password,
            labelTextId = R.string.signup_password_hint,
            iconId = R.drawable.password,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            password = it
        }
        Spacer(modifier = Modifier.height(12.dp))
        SignupInputField(
            value = confirmPassword,
            labelTextId = R.string.signup_confirm_password_hint,
            iconId = R.drawable.password,
            isPassword = true,
            keyboardType = KeyboardType.Password,
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            confirmPassword = it
        }
        Spacer(modifier = Modifier.height(20.dp))
        SignupButton(
            isEnabled = password.isNotBlank() && email.isNotBlank() && name.isNotBlank() && confirmPassword.isNotBlank() && password == confirmPassword && isEmailValid(email),
            modifier = Modifier
                .fillMaxWidth(.5f)
                .heightIn(min = 48.dp)
        ) {
            onRegister(name, email, password)
        }
        FillOrMinHeight(minHeight = 24.dp)
        Row(
            modifier = Modifier.clickable {
                goToLogin()
            },
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(id = R.string.signup_go_to_login_1),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(id = R.string.signup_go_to_login_2),
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
private fun SignupButton(
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
            text = stringResource(id = R.string.signup_signup_btn),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun SignupInputField(
    value: String,
    @StringRes labelTextId: Int,
    @DrawableRes iconId: Int,
    isPassword: Boolean,
    keyboardType: KeyboardType,
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
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

@PreviewAnnotations
@Composable
private fun SignupPanePreview() {
    ColingoTheme {
        Surface {
            SignupPane(onRegister = {_ ,_ ,_ -> }, goToLogin = {  }, isEmailValid = { true })
        }
    }
}