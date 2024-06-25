package com.colledk.colingo.ui.compose

import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.movableContentOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.util.fastForEach
import com.colledk.colingo.ui.ColingoAppState
import com.colledk.colingo.ui.navigation.ColingoNavHost
import com.colledk.colingo.ui.rememberColingoAppState

@Composable
internal fun ColingoApp(
    appState: ColingoAppState = rememberColingoAppState()
) {
    val currentDestination = appState.currentTopLevelDestination

    val app = movableContentOf {
        ColingoNavHost(appState = appState)
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
