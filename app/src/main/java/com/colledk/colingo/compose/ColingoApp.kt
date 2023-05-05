package com.colledk.colingo.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.colledk.colingo.ColingoAppState
import com.colledk.colingo.navigation.ColingoNavHost
import com.colledk.colingo.rememberColingoAppState
import com.colledk.onboarding.ui.navigation.navigateToOnboardingGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ColingoApp(
    appState: ColingoAppState = rememberColingoAppState()
) {
    val navController = appState.navController

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { padding ->
        Column(modifier = Modifier.padding(paddingValues = padding)) {
            ColingoNavHost(appState = appState)

            LaunchedEffect(key1 = true) {
                navController.navigateToOnboardingGraph()
            }
        }
    }
}