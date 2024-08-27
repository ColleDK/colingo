package com.colledk.colingo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.colledk.colingo.ui.compose.ColingoApp
import com.colledk.theme.ColingoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val dynamicThemingEnabled by viewModel.dynamicThemeEnabled.collectAsState()
            val darkThemingEnabled by viewModel.darkThemeEnabled.collectAsState()

            installSplashScreen().setKeepOnScreenCondition(
                condition = { dynamicThemingEnabled == null }
            )

            dynamicThemingEnabled?.let {
                ColingoTheme(dynamicColor = it, darkTheme = darkThemingEnabled) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.surface
                    ) {
                        ColingoApp()
                    }
                }
            }
        }
    }
}