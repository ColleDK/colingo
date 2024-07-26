package com.colledk.colingo.ui.compose.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import java.util.Locale

@Composable
internal fun SwitchLanguagePane(
    onSwitchLanguage: (locale: Locale) -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLocale = androidx.compose.ui.text.intl.Locale.current.platformLocale
    val remainingLocales by remember {
        derivedStateOf {
            Locale.getAvailableLocales()
                .filter { it != currentLocale && it.displayLanguage.isNotBlank() }
                .distinctBy { it.displayLanguage }.sortedBy { it.displayLanguage }
        }
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp)
    ) {
        item {
            LocaleItem(
                locale = currentLocale,
                isSelected = true,
                modifier = Modifier.fillParentMaxWidth(),
                onItemSelected = { onSwitchLanguage(currentLocale) }
            )
        }
        items(remainingLocales) { locale ->
            LocaleItem(
                locale = locale,
                isSelected = false,
                modifier = Modifier.fillParentMaxWidth(),
                onItemSelected = { onSwitchLanguage(locale) }
            )
        }
    }
}

@Composable
private fun LocaleItem(
    locale: Locale,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onItemSelected: () -> Unit
) {
    ListItem(
        modifier = modifier.clickable { onItemSelected() },
        headlineContent = {
            Text(text = locale.displayLanguage)
        },
        leadingContent = {
            Text(
                text = locale.language.uppercase(),
                color = MaterialTheme.colorScheme.surface,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSurface)
                    .padding(4.dp)
            )
        },
        trailingContent = {
            if (isSelected) {
                Icon(
                    painter = painterResource(id = com.colledk.home.R.drawable.checkmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    )
}