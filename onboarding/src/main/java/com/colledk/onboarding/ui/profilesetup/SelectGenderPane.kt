package com.colledk.onboarding.ui.profilesetup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import com.colledk.onboarding.R
import com.colledk.user.domain.model.Gender

@Composable
internal fun SelectGender(
    selectedGender: Gender?,
    onGenderSelected: (gender: Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileSetup(
        titleId = R.string.select_gender_title,
        subtitleId = R.string.select_gender_subtitle,
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(.8f)
        ) {
            Gender.entries.fastForEach {
                GenderItem(gender = it, isSelected = selectedGender == it) {
                    onGenderSelected(it)
                }
            }
        }
    }
}

@Composable
private fun GenderItem(
    gender: Gender,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onGenderSelected: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(
                text = stringResource(id = gender.nameId),
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        },
        trailingContent = {
            if (isSelected) {
                Icon(
                    painter = painterResource(id = R.drawable.checkmark),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        leadingContent = {
            val icon = when (gender) {
                Gender.MALE -> R.drawable.male
                Gender.FEMALE -> R.drawable.female
                Gender.OTHER -> R.drawable.nonbinary
            }

            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.size(24.dp)
            )
        },
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .clickable { onGenderSelected() },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}