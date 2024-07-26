package com.colledk.onboarding.ui.profilesetup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.colledk.onboarding.R
import com.colledk.user.domain.model.LanguageProficiency
import com.colledk.user.domain.model.UserLanguage

@Composable
internal fun SelectLanguagesPane(
    selectedLanguages: List<UserLanguage>,
    onAddLanguage: (language: UserLanguage) -> Unit,
    onRemoveLanguage: (language: UserLanguage) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDialog by remember {
        mutableStateOf(false)
    }

    ProfileSetup(
        titleId = R.string.add_languages_title,
        subtitleId = R.string.add_languages_subtitle,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            selectedLanguages.fastForEach { language ->
                UserLanguageItem(userLanguage = language) {
                    onRemoveLanguage(language)
                }
            }
            AddUserLanguage {
                showDialog = true
            }
        }
    }

    if (showDialog) {
        SelectNewLanguage(
            selectedCountries = selectedLanguages.map { it.language.displayName },
            onSelect = {
                onAddLanguage(it)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
            }
        )
    }
}

@Composable
private fun AddUserLanguage(modifier: Modifier = Modifier, onAddClick: () -> Unit) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(id = R.string.add_languages_add_language),
                style = MaterialTheme.typography.bodyLarge,
                fontStyle = FontStyle.Italic
            )
        },
        leadingContent = {
            Icon(
                painter = painterResource(id = R.drawable.add),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onAddClick() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

@Composable
private fun SelectNewLanguage(
    selectedCountries: List<String>,
    modifier: Modifier = Modifier,
    countries: List<java.util.Locale> = java.util.Locale.getAvailableLocales().toList(),
    onSelect: (UserLanguage) -> Unit,
    onDismiss: () -> Unit
) {
    val languages by remember {
        derivedStateOf {
            countries.filter { !selectedCountries.contains(it.displayLanguage) }
                .distinctBy { it.displayLanguage }.sortedBy { it.displayLanguage }
        }
    }

    var selectedLanguage by remember {
        mutableStateOf<java.util.Locale?>(null)
    }
    var selectedProficiency by remember {
        mutableStateOf<LanguageProficiency?>(null)
    }

    var showLanguageMenu by remember {
        mutableStateOf(false)
    }

    var showProficiencyMenu by remember {
        mutableStateOf(false)
    }

    AlertDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        title = {
            Box {
                Text(
                    text = selectedLanguage?.displayName ?: stringResource(id = R.string.add_languages_select_language),
                    modifier = Modifier.clickable {
                        showLanguageMenu = true
                    }
                )

                // TODO rewrite to not being dropdown
                DropdownMenu(
                    expanded = showLanguageMenu,
                    onDismissRequest = { showLanguageMenu = false }
                ) {
                    languages.fastForEach { locale ->
                        DropdownMenuItem(
                            text = {
                                Text(text = locale.displayLanguage)
                            },
                            leadingIcon = {
                                Text(
                                    text = locale.language.uppercase(),
                                    color = MaterialTheme.colorScheme.surface,
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(MaterialTheme.colorScheme.onSurface)
                                        .padding(2.dp)
                                )
                            },
                            onClick = {
                                selectedLanguage = locale.also {
                                    showLanguageMenu = false
                                }
                            }
                        )
                    }
                }
            }
        },
        text = {
            Box {
                Text(
                    text = selectedProficiency?.name?.lowercase()?.capitalize(Locale.current)
                        ?: stringResource(id = R.string.add_languages_select_proficiency),
                    modifier = Modifier.clickable {
                        showProficiencyMenu = true
                    }
                )
                DropdownMenu(
                    expanded = showProficiencyMenu,
                    onDismissRequest = { showProficiencyMenu = false }
                ) {
                    LanguageProficiency.entries.fastForEach { proficiency ->
                        DropdownMenuItem(
                            text = {
                                Text(text = proficiency.name.lowercase().capitalize(Locale.current))
                            },
                            leadingIcon = {
                                val icon = when (proficiency) {
                                    LanguageProficiency.BEGINNER -> R.drawable.proficiency_low
                                    LanguageProficiency.INTERMEDIATE -> R.drawable.proficiency_medium
                                    LanguageProficiency.FLUENT -> R.drawable.proficiency_high
                                }
                                Icon(
                                    painter = painterResource(id = icon),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            onClick = {
                                selectedProficiency = proficiency.also {
                                    showProficiencyMenu = false
                                }
                            }
                        )
                    }
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.add_language_cancel))
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onSelect(
                        UserLanguage(
                            language = selectedLanguage!!,
                            proficiency = selectedProficiency!!
                        )
                    )
                },
                enabled = selectedLanguage != null && selectedProficiency != null
            ) {
                Text(text = stringResource(id = R.string.add_language_select))
            }
        }
    )
}

@Composable
private fun UserLanguageItem(
    userLanguage: UserLanguage,
    modifier: Modifier = Modifier,
    onRemoveClicked: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = userLanguage.language.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        },
        leadingContent = {
            Text(
                text = userLanguage.language.language.uppercase(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
                    .padding(4.dp)
            )
        },
        supportingContent = {
            Text(
                text = userLanguage.proficiency.name.lowercase().capitalize(Locale.current),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        trailingContent = {
            IconButton(
                onClick = onRemoveClicked,
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSecondaryContainer)
                        .padding(4.dp)
                )
            }
        },
        modifier = modifier.clip(RoundedCornerShape(4.dp)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}