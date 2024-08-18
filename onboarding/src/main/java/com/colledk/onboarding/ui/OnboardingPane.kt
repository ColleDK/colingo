package com.colledk.onboarding.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.colledk.onboarding.R

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
fun OnboardingPane(
    onGoToSetup: () -> Unit,
    onGoToFrontpage: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val login by viewModel.login.collectAsState(null)
    val error by viewModel.error.collectAsState(null)
    val goToSetup by viewModel.goToProfileSetup.collectAsState(null)
    val resetPassword by viewModel.resetPasswordSuccess.collectAsState(null)

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = login) {
        if (login != null) {
            onGoToFrontpage()
        }
    }

    LaunchedEffect(key1 = goToSetup) {
        if (goToSetup != null) {
            onGoToSetup()
        }
    }

    LaunchedEffect(key1 = error) {
        if (error != null) {
            snackbarHostState.showSnackbar(
                error?.localizedMessage ?: "Something went wrong. Please try again"
            )
        }
    }

    LaunchedEffect(key1 = resetPassword) {
        if (resetPassword != null) {
            snackbarHostState.showSnackbar("An email has been sent to you with instructions on how to reset your password. Please check your spam-box if you cannot find it!")
        }

    }

    val navigator = rememberListDetailPaneScaffoldNavigator<OnboardingDestination>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        ListDetailPaneScaffold(
            modifier = modifier
                .padding(it)
                .then(
                    Modifier.background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.surfaceTint,
                                MaterialTheme.colorScheme.secondaryContainer
                            )
                        )
                    )
                ),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    OnboardingContent(
                        onGoToLogin = {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                OnboardingDestination.LOG_IN
                            )
                        },
                        onGoToSignup = {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                OnboardingDestination.SIGN_UP
                            )
                        }
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    navigator.currentDestination?.content?.let {
                        Scaffold(
                            topBar = {
                                if (navigator.canNavigateBack()) {
                                    TopAppBar(
                                        title = {},
                                        navigationIcon = {
                                            IconButton(onClick = { navigator.navigateBack() }) {
                                                Icon(
                                                    painter = painterResource(id = R.drawable.back_arrow),
                                                    contentDescription = stringResource(id = R.string.onboarding_go_back)
                                                )
                                            }
                                        },
                                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                                    )
                                }
                            },
                            containerColor = Color.Transparent
                        ) { padding ->
                            if (it == OnboardingDestination.LOG_IN) {
                                LoginPane(
                                    onForgotPassword = viewModel::forgotPassword,
                                    onLogin = viewModel::login,
                                    onGoToSignup = {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            OnboardingDestination.SIGN_UP
                                        )
                                    },
                                    modifier = Modifier.padding(padding),
                                    isEmailValid = viewModel::isEmailValid
                                )
                            } else {
                                SignupPane(
                                    onRegister = viewModel::createUser,
                                    goToLogin = {
                                        navigator.navigateTo(
                                            ListDetailPaneScaffoldRole.Detail,
                                            OnboardingDestination.LOG_IN
                                        )
                                    },
                                    modifier = Modifier.padding(padding),
                                    isEmailValid = viewModel::isEmailValid
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

@Composable
private fun OnboardingContent(
    onGoToLogin: () -> Unit,
    onGoToSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Image(
            painter = painterResource(id = R.drawable.world),
            contentDescription = null // Not needed for talkback
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.onboarding_app_title),
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(modifier = Modifier.height(44.dp))
        OnboardingButton(
            btnText = stringResource(id = R.string.onboarding_login_btn),
            modifier = Modifier.fillMaxWidth(.8f),
            onClick = onGoToLogin
        )
        Spacer(modifier = Modifier.height(12.dp))
        OnboardingButton(
            btnText = stringResource(id = R.string.onboarding_signup_btn),
            modifier = Modifier.fillMaxWidth(.8f),
            onClick = onGoToSignup
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))
//        TextButton(onClick = onAnonLogin) {
//            Text(
//                text = stringResource(id = R.string.onboarding_guest_btn),
//                style = MaterialTheme.typography.labelSmall,
//                color = MaterialTheme.colorScheme.onPrimaryContainer,
//                modifier = Modifier.alpha(.7f)
//            )
//        }
    }
}

@Composable
private fun OnboardingButton(
    btnText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(size = 8.dp)
    ) {
        Text(
            text = btnText,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}