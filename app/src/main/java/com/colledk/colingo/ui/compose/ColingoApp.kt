package com.colledk.colingo.ui.compose

import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.movableContentOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.util.fastForEach
import com.colledk.colingo.ui.ColingoAppState
import com.colledk.colingo.ui.navigation.ColingoNavHost
import com.colledk.colingo.ui.navigation.homePaneRoute
import com.colledk.colingo.ui.rememberColingoAppState
import com.colledk.onboarding.navigation.onboardingGraphRoute
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
internal fun ColingoApp(
    appState: ColingoAppState = rememberColingoAppState()
) {
    val currentDestination = appState.currentTopLevelDestination

    val startDestination by remember {
        derivedStateOf {
            if (Firebase.auth.currentUser?.uid == null) onboardingGraphRoute else homePaneRoute
        }
    }

    val app = movableContentOf {
        ColingoNavHost(
            appState = appState,
            startDestination = startDestination
        )
    }

    if (currentDestination != null) {
        NavigationSuiteScaffold(
            navigationSuiteItems = {
                appState.topLevelDestinations.fastForEach { destination ->
                    val isSelected = destination == currentDestination
                    item(
                        icon = {
                            Icon(
                                imageVector = destination.icon,
                                contentDescription = destination.iconDescription
                            )
                        },
                        label = { Text(text = stringResource(id = destination.titleText), fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                        selected = isSelected,
                        onClick = { appState.navigateToTopLevelDestination(destination) }
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
