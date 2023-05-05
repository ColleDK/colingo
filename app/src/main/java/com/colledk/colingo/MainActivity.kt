package com.colledk.colingo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.colledk.colingo.compose.ColingoApp
import com.colledk.colingo.navigation.ColingoNavHost
import com.colledk.colingo.ui.theme.ColingoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ColingoTheme {
                ColingoApp()
            }
        }
    }
}