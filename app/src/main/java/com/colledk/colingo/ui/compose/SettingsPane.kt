package com.colledk.colingo.ui.compose

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.colledk.colingo.R
import com.colledk.colingo.data.Setting
import com.colledk.colingo.domain.model.SettingsOption
import com.colledk.colingo.domain.model.TopSetting
import com.colledk.colingo.ui.compose.settings.AboutPane
import com.colledk.colingo.ui.compose.settings.ReportBugPane
import com.colledk.colingo.ui.compose.settings.SettingUiState
import com.colledk.colingo.ui.compose.settings.SwitchLanguagePane
import com.colledk.colingo.ui.compose.settings.ThemingSettingsPane
import com.google.android.play.core.review.ReviewInfo
import com.google.android.play.core.review.ReviewManager
import com.google.android.play.core.review.ReviewManagerFactory
import timber.log.Timber

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsPane(
    onSwitchLanguage: (locale: java.util.Locale) -> Unit,
    selectedSetting: TopSetting?,
    settingsState: SettingUiState,
    retrieveSettings: (settings: List<Setting<*>>) -> Unit,
    updateSetting: (setting: Setting<*>, value: Any) -> Unit,
    selectSetting: (TopSetting) -> Unit,
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator(
        initialDestinationHistory = listOfNotNull(
            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
            ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.Detail, selectedSetting).takeIf {
                selectedSetting != null
            }
        )
    )

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                SettingsContent(
                    onSwitchLanguage = {
                        selectSetting(TopSetting.SWITCH_LANGUAGE)
                        navigator.navigateTo(
                            pane = ListDetailPaneScaffoldRole.Detail,
                            content = TopSetting.SWITCH_LANGUAGE
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
                    selectSetting(TopSetting.valueOf(it.name))
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        content = TopSetting.valueOf(it.name)
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { setting ->
                    when (setting) {
                        TopSetting.SWITCH_LANGUAGE -> SwitchLanguagePane(onSwitchLanguage = onSwitchLanguage)
                        TopSetting.THEMING -> ThemingSettingsPane(
                            settingUiState = settingsState,
                            retrieveSettings = retrieveSettings,
                            updateSetting = updateSetting
                        )
                        TopSetting.NOTIFICATIONS -> {}
                        TopSetting.PERMISSIONS -> {}
                        TopSetting.ACCESSIBILITY -> {}
                        TopSetting.ABOUT -> AboutPane(modifier = Modifier.fillMaxSize())
                        TopSetting.REPORT_BUG -> ReportBugPane(modifier = Modifier.fillMaxSize())
                        TopSetting.RATE_THE_APP -> {
                            val localContext = LocalContext.current

                            val reviewManager = remember {
                                ReviewManagerFactory.create(localContext)
                            }

                            val reviewInfo = rememberReviewTask(reviewManager = reviewManager)

                            LaunchedEffect(key1 = reviewInfo) {
                                reviewInfo?.let {
                                    reviewManager.launchReviewFlow(localContext as Activity, reviewInfo)
                                }
                            }
                        }
                        TopSetting.LOG_OUT -> {
                            onLogOut()
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsContent(
    onSwitchLanguage: () -> Unit,
    modifier: Modifier = Modifier,
    onSettingClick: (SettingsOption) -> Unit
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.settings_change),
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 24.dp)
        ) {
            item {
                ListItem(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .clickable { onSwitchLanguage() },
                    headlineContent = {
                        Text(text = stringResource(id = R.string.settings_switch_language))
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.language),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    supportingContent = {
                        Text(text = Locale.current.platformLocale.displayLanguage)
                    },
                    trailingContent = {
                        Icon(
                            painter = painterResource(id = R.drawable.arrow_right),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            }
            items(SettingsOption.entries) { setting ->
                ListItem(
                    headlineContent = {
                        Text(
                            text = stringResource(id = setting.titleId),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    trailingContent = {
                        if (setting != SettingsOption.LOG_OUT) {
                            Icon(
                                painter = painterResource(id = R.drawable.arrow_right),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    },
                    leadingContent = {
                        Icon(
                            painter = painterResource(id = setting.iconId),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onSettingClick(setting)
                        }
                )
            }
        }
    }
}

@Composable
private fun rememberReviewTask(reviewManager: ReviewManager): ReviewInfo? {
    var reviewInfo: ReviewInfo? by remember {
        mutableStateOf(null)
    }
    reviewManager.requestReviewFlow().addOnCompleteListener {
        if (it.isSuccessful) {
            reviewInfo = it.result
        }
    }.addOnFailureListener {
        Timber.e(it)
    }

    return reviewInfo
}