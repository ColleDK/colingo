package com.colledk.onboarding.ui.profilesetup

import android.location.Address
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.colledk.common.PastOrPresentSelectableDate
import com.colledk.onboarding.R
import com.colledk.user.domain.isUnknown
import com.colledk.user.domain.toText
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

@Composable
internal fun AddDescriptionPane(
    birthday: DateTime?,
    address: Address?,
    description: String,
    updateBirthday: (time: Long) -> Unit,
    getLocation: () -> Unit,
    updateDescription: (description: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ProfileSetup(
        titleId = R.string.add_description_title,
        subtitleId = R.string.add_description_subtitle,
        modifier = modifier
    ) {
        var showDatePicker by remember {
            mutableStateOf(false)
        }

        var showLocationSelector by remember {
            mutableStateOf(false)
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(.8f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ListItem(
                headlineContent = {
                    Text(
                        text = birthday?.let { birthday.toString(DateTimeFormat.mediumDate()) } ?: stringResource(id = R.string.add_description_birthday_hint),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.birthday),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { showDatePicker = true },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )

            if (showDatePicker) {
                BirthdaySelector(
                    onSelect = {
                        updateBirthday(it)
                        showDatePicker = false
                    },
                    onDismiss = { showDatePicker = false },
                    currentSelected = birthday?.millis
                )
            }
            // Location item
            ListItem(
                headlineContent = {
                    Text(
                        text = if (address.isUnknown()) stringResource(id = R.string.add_description_location_hint) else address.toText(),
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                leadingContent = {
                    Icon(
                        painter = painterResource(id = R.drawable.location),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .clickable { showLocationSelector = true },
                colors = ListItemDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )

            if (showLocationSelector) {
                PermissionRequester(
                    onPermissionGranted = getLocation,
                    onPermissionDenied = { /*TODO*/ },
                    onPermissionRevoked = { /*TODO*/ },
                    permissions = listOf(
                        android.Manifest.permission.ACCESS_COARSE_LOCATION,
                        android.Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }

            DescriptionField(
                value = description,
                labelTextId = R.string.add_description_description_hint,
                iconId = R.drawable.description,
                modifier = Modifier.fillMaxWidth()
            ) {
                updateDescription(it)
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit,
    onPermissionRevoked: () -> Unit,
    permissions: List<String>
) {
    val permissionState = rememberMultiplePermissionsState(permissions = permissions)

    val grantedPermissions by remember {
        derivedStateOf {
            permissionState.permissions.filter {
                it.status.isGranted
            }
        }
    }

    LaunchedEffect(key1 = grantedPermissions) {
        if (grantedPermissions.isNotEmpty()) {
            onPermissionGranted()
        }
    }

    LaunchedEffect(key1 = permissionState) {
        val allPermissionRevoked = permissionState.permissions.size == permissionState.revokedPermissions.size

        val permissionToRequest = permissionState.permissions.filterNot {
            it.status.isGranted
        }

        if (permissionToRequest.isNotEmpty()) permissionState.launchMultiplePermissionRequest()

        if (allPermissionRevoked) {
            onPermissionRevoked()
        } else {
            if (permissionState.allPermissionsGranted) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BirthdaySelector(
    currentSelected: Long?,
    onSelect: (time: Long) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state = rememberDatePickerState(initialSelectedDateMillis = currentSelected, selectableDates = PastOrPresentSelectableDate)

    DatePickerDialog(
        modifier = modifier,
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    state.selectedDateMillis?.let {
                        onSelect(it)
                    }
                }
            ) {
                Text(text = stringResource(id = R.string.add_description_birthday_select))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.add_description_birthday_cancel))
            }
        }
    ) {
        DatePicker(state = state)
    }
}

@Composable
private fun DescriptionField(
    value: String,
    @StringRes labelTextId: Int,
    @DrawableRes iconId: Int,
    modifier: Modifier = Modifier,
    onValueChanged: (String) -> Unit
) {
    TextField(
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(
                text = stringResource(id = labelTextId),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id = iconId),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            unfocusedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            focusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer,
            unfocusedTextColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        modifier = modifier
    )
}
