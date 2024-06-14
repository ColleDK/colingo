package com.colledk.onboarding.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeightIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.colledk.onboarding.R

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OnboardingPane(modifier: Modifier = Modifier, onFinishOnboarding: () -> Unit) {
    val navigator = rememberListDetailPaneScaffoldNavigator<OnboardingDestination>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = modifier.then(
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
                    },
                    onGoToGoogle = { /*TODO*/ },
                    onGoToFacebook = { /*TODO*/ })
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    if (it == OnboardingDestination.LOG_IN) {
                        LoginPane(
                            onForgotPassword = {},
                            onLogin = { _, _ -> },
                            onGoToSignup = {
                                navigator.navigateTo(
                                    ListDetailPaneScaffoldRole.Detail,
                                    OnboardingDestination.SIGN_UP
                                )
                            }
                        )
                    } else {
                        SignupPane(onRegister = { _, _, _ -> }, goToLogin = {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                OnboardingDestination.LOG_IN
                            )
                        })
                    }
                }
            }
        }
    )
}

@Composable
private fun OnboardingContent(
    onGoToLogin: () -> Unit,
    onGoToSignup: () -> Unit,
    onGoToGoogle: () -> Unit,
    onGoToFacebook: () -> Unit,
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
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(.8f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
            )
            Text(
                text = stringResource(id = R.string.onboarding_other_title),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(
                modifier = Modifier
                    .height(1.dp)
                    .weight(1f)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        OnboardingButton(
            btnText = stringResource(id = R.string.onboarding_google_btn),
            modifier = Modifier.fillMaxWidth(.8f),
            onClick = onGoToGoogle
        )
        Spacer(modifier = Modifier.height(12.dp))
        OnboardingButton(
            btnText = stringResource(id = R.string.onboarding_facebook_btn),
            modifier = Modifier.fillMaxWidth(.8f),
            onClick = onGoToFacebook
        )
        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.onboarding_guest_btn),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.alpha(.7f)
        )
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