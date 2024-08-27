package com.colledk.colingo.ui.compose.settings

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.colledk.colingo.R
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun SwitchLanguagePane(
    onSwitchLanguage: (locale: Locale) -> Unit,
    modifier: Modifier = Modifier
) {
    val allLanguages by remember {
        derivedStateOf {
            Locale.getAvailableLocales()
                .filter { it.displayLanguage.isNotBlank() }
                .distinctBy { it.displayLanguage }.sortedBy { it.displayLanguage }
        }
    }

    val currentLocale = androidx.compose.ui.text.intl.Locale.current.platformLocale

    val remainingLocales by remember {
        derivedStateOf {
            allLanguages.filterNot { it.language == currentLocale.language }
                .groupBy { it.displayLanguage.first() }
        }
    }
    LazyColumn(
        modifier = modifier.padding(top = 24.dp),
        contentPadding = PaddingValues(24.dp)
    ) {
        stickyHeader {
            Surface(
                modifier = Modifier.fillParentMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.selected_language), fontWeight = FontWeight.Bold)
            }
        }
        item {
            LocaleItem(
                locale = currentLocale,
                isSelected = true,
                modifier = Modifier.fillParentMaxWidth(),
                onItemSelected = { onSwitchLanguage(currentLocale) }
            )
        }
        remainingLocales.forEach { (startLetter, locales) ->
            stickyHeader {
                Surface(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    Text(text = "$startLetter", fontWeight = FontWeight.Bold)
                }
            }
            items(locales) { locale ->
                LocaleItem(
                    locale = locale,
                    isSelected = false,
                    modifier = Modifier.fillParentMaxWidth(),
                    onItemSelected = { onSwitchLanguage(locale) }
                )
            }
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