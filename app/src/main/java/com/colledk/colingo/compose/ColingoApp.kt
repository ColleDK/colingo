package com.colledk.colingo.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.colledk.colingo.ColingoAppState
import com.colledk.colingo.navigation.ColingoNavHost
import com.colledk.colingo.navigation.TopLevelDestination
import com.colledk.colingo.rememberColingoAppState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColingoApp(
    appState: ColingoAppState = rememberColingoAppState()
) {
    val navController = appState.navController

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            ColingoAppBottomBar(
                topLevelDestinations = appState.topLevelDestinations,
                currentTopLevelDestination = appState.currentTopLevelDestination
            ) {
                appState.navigateToTopLevelDestination(topLevelDestination = it)
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(paddingValues = padding)) {
            ColingoNavHost(appState = appState)

        }
    }
}

@Composable
private fun ColingoAppBottomBar(
    currentTopLevelDestination: TopLevelDestination?,
    topLevelDestinations: List<TopLevelDestination>,
    onDestinationClicked: (TopLevelDestination) -> Unit
) {
    NavigationBar(
        modifier = Modifier
            .wrapContentHeight()
            .fillMaxWidth()
    ) {
        topLevelDestinations.forEach {
            val isSelected = it == currentTopLevelDestination

            NavigationBarItem(
                selected = isSelected,
                onClick = { onDestinationClicked(it) },
                icon = {
                    val currentIcon = if (isSelected) {
                        it.selectedIcon
                    } else {
                        it.unselectedIcon
                    }

                    Icon(
                        imageVector = currentIcon,
                        contentDescription = stringResource(id = it.iconTextId)
                    )
                },
                label = {
                    Text(
                        text = stringResource(id = it.titleTextId),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            )
        }
    }
}

@Composable
internal fun HomeScreen() {
    Text(text = "HOME SCREEN", style = MaterialTheme.typography.headlineLarge)
}