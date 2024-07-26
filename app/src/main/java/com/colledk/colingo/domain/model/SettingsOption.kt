package com.colledk.colingo.domain.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Stable
import com.colledk.colingo.R
import kotlinx.parcelize.Parcelize

@Parcelize
enum class TopSetting : Parcelable {
    SWITCH_LANGUAGE,
    BETA,
    NOTIFICATIONS,
    PERMISSIONS,
    ACCESSIBILITY,
    ABOUT,
    RATE_THE_APP,
    REPORT_BUG,
    LOG_OUT
}

@Stable
enum class SettingsOption(
    @StringRes val titleId: Int,
    @DrawableRes val iconId: Int
) {
    BETA(titleId = R.string.setting_beta_title, iconId = R.drawable.beta),
    NOTIFICATIONS(titleId = R.string.setting_notifications_title, iconId = R.drawable.notifications),
    PERMISSIONS(titleId = R.string.setting_permissions_title, iconId = R.drawable.permissions),
    ACCESSIBILITY(titleId = R.string.setting_accessibility_title, iconId = R.drawable.accessibility),
    ABOUT(titleId = R.string.setting_about_title, iconId = R.drawable.about),
    RATE_THE_APP(titleId = R.string.setting_rate_title, iconId = R.drawable.rate),
    REPORT_BUG(titleId = R.string.setting_report_title, iconId = R.drawable.bug_report),
    LOG_OUT(titleId = R.string.setting_logout_title, R.drawable.logout)
}
