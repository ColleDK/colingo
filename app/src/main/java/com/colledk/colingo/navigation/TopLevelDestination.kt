package com.colledk.colingo.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import com.colledk.colingo.R

enum class TopLevelDestination(
    val icon: ImageVector,
    val iconDescription: String,
    @StringRes val titleText: Int
) {
    HOME(
        icon = Icons.Filled.Home,
        iconDescription = "Homepage",
        titleText = R.string.destination_home_label
    ),
    EXPLORE(
        icon = Icons.Filled.Search,
        iconDescription = "Exploration",
        titleText = R.string.destination_explore_label
    ),
    CHAT(
        icon = Icons.AutoMirrored.Filled.Chat,
        iconDescription = "Chat",
        titleText = R.string.destination_chat_label
    ),
    PROFILE(
        icon = Icons.Filled.Person,
        iconDescription = "Profile",
        titleText = R.string.destination_profile_label
    ),
    SETTINGS(
        icon = Icons.Filled.Settings,
        iconDescription = "Settings",
        titleText = R.string.destination_settings_label
    )
}