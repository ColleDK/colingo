package com.colledk.colingo.ui.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.hilt.navigation.compose.hiltViewModel
import com.colledk.colingo.ui.AppViewModel
import com.colledk.colingo.ui.ColingoAppState
import com.colledk.colingo.ui.navigation.ColingoNavHost
import com.colledk.colingo.ui.navigation.TopLevelDestination
import com.colledk.colingo.ui.rememberColingoAppState
import com.colledk.home.navigation.Home
import com.colledk.onboarding.navigation.Onboarding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import timber.log.Timber

@Composable
internal fun ColingoApp(
    appState: ColingoAppState = rememberColingoAppState(),
    appViewModel: AppViewModel = hiltViewModel()
) {
    val currentDestination = appState.currentTopLevelDestination

    val startDestination by remember {
        derivedStateOf {
            if (appState.currentUserId == null) Onboarding else Home
        }
    }

    LaunchedEffect(key1 = startDestination) {
        if (startDestination == Home && currentDestination == null) {
            appState.navigateToTopLevelDestination(TopLevelDestination.HOME)
        }
    }

    val message by appViewModel.messages.collectAsState(initial = null)

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(key1 = message) {
        if (message != null) {
            snackbarHostState.showSnackbar(message!!)
        }
    }

    val app = movableContentOf {
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            }
        ) {
            ColingoNavHost(
                appState = appState,
                startDestination = startDestination,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            )
        }
    }

    if (currentDestination != null) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                appState.topLevelDestinations.fastForEach { destination ->
                    val isSelected = destination == currentDestination
                    item(
                        icon = {
                            Icon(
                                painter = painterResource(id = if (isSelected) destination.selectedIcon else destination.icon),
                                contentDescription = destination.iconDescription,
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text(text = stringResource(id = destination.titleText), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        selected = isSelected,
                        onClick = {
                            appState.navigateToTopLevelDestination(destination)
                        }
                    )
                }
            }
        ) {
            app()
        }
    } else {
        app()
    }
}
