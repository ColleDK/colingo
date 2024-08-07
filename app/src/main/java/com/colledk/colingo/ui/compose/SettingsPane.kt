package com.colledk.colingo.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import com.colledk.colingo.R
import com.colledk.colingo.domain.model.SettingsOption
import com.colledk.colingo.domain.model.TopSetting
import com.colledk.colingo.ui.compose.settings.BetaSettings
import com.colledk.colingo.ui.compose.settings.SwitchLanguagePane

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsPane(
    onSwitchLanguage: (locale: java.util.Locale) -> Unit,
    modifier: Modifier = Modifier,
    onLogOut: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<TopSetting>()

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
                        navigator.navigateTo(
                            pane = ListDetailPaneScaffoldRole.Detail,
                            content = TopSetting.SWITCH_LANGUAGE
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) {
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
                        TopSetting.BETA -> BetaSettings()
                        TopSetting.NOTIFICATIONS -> TODO()
                        TopSetting.PERMISSIONS -> TODO()
                        TopSetting.ACCESSIBILITY -> TODO()
                        TopSetting.ABOUT -> TODO()
                        TopSetting.RATE_THE_APP -> TODO()
                        TopSetting.REPORT_BUG -> TODO()
                        TopSetting.LOG_OUT -> {
                            LaunchedEffect(key1 = Unit) {
                                onLogOut()
                            }
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
                        text = "Change settings",
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
                        Text(text = "Switch language")
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