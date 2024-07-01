package com.colledk.colingo.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.colledk.colingo.R
import com.colledk.colingo.domain.SettingsOption
import com.colledk.onboarding.ui.OnboardingDestination

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun SettingsPane(modifier: Modifier = Modifier) {
    val navigator = rememberListDetailPaneScaffoldNavigator<SettingsOption>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                SettingsContent(Modifier.fillMaxSize()) {
                    navigator.navigateTo(
                        pane = ListDetailPaneScaffoldRole.Detail,
                        content = it
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let {
                    // TODO figure out what settings to have
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SettingsContent(
    modifier: Modifier = Modifier,
    onSettingClick: (SettingsOption) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 24.dp)
    ) {
        stickyHeader {
            Surface(modifier = Modifier.fillParentMaxWidth().padding(horizontal = 16.dp)) {
                Text(
                    text = "Change settings",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
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
                    Icon(
                        painter = painterResource(id = R.drawable.arrow_right),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = setting.iconId),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.size(24.dp)
                    )
                },
                modifier = Modifier.fillMaxWidth().clickable {
                    onSettingClick(setting)
                }
            )
        }
    }
}