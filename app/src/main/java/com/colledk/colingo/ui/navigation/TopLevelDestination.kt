package com.colledk.colingo.ui.navigation

import androidx.annotation.DrawableRes
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
    @DrawableRes val icon: Int,
    @DrawableRes val selectedIcon: Int,
    val iconDescription: String,
    @StringRes val titleText: Int
) {
    HOME(
        icon = R.drawable.home,
        selectedIcon = R.drawable.home_filled,
        iconDescription = "Homepage",
        titleText = R.string.destination_home_label
    ),
    EXPLORE(
        icon = R.drawable.community,
        selectedIcon = R.drawable.community_filled,
        iconDescription = "Exploration",
        titleText = R.string.destination_explore_label
    ),
    CHAT(
        icon = R.drawable.chat,
        selectedIcon = R.drawable.chat_filled,
        iconDescription = "Chat",
        titleText = R.string.destination_chat_label
    ),
    PROFILE(
        icon = R.drawable.profile,
        selectedIcon = R.drawable.profile_filled,
        iconDescription = "Profile",
        titleText = R.string.destination_profile_label
    ),
    SETTINGS(
        icon = R.drawable.settings,
        selectedIcon = R.drawable.settings_filled,
        iconDescription = "Settings",
        titleText = R.string.destination_settings_label
    )
}