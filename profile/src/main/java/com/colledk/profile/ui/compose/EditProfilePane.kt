package com.colledk.profile.ui.compose

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.colledk.profile.R
import com.colledk.profile.ui.uistates.EditProfileUiState
import com.colledk.user.domain.isUnknown
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.UserLanguage
import com.colledk.user.domain.toText
import org.joda.time.format.DateTimeFormat

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun EditProfilePane(
    uiState: EditProfileUiState,
    onSave: () -> Unit,
    onAddPicture: (uri: Uri) -> Unit,
    onRemovePicture: (uri: Uri) -> Unit,
    onEditDescription: (description: String) -> Unit,
    onSwitchBirthday: (time: Long) -> Unit,
    onUpdateLocation: () -> Unit,
    onAddLanguage: (language: UserLanguage) -> Unit,
    onRemoveLanguage: (language: UserLanguage) -> Unit,
    onAddTopic: (topic: Topic) -> Unit,
    onRemoveTopic: (topic: Topic) -> Unit,
    onChangeName: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {},
                actions = {
                    IconButton(onClick = onSave) {
                        Icon(
                            painter = painterResource(id = R.drawable.save),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            )
        }
    ) { padding ->
        var showDatePicker by remember {
            mutableStateOf(false)
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            item {
                ProfilePictures(
                    isInEditMode = true,
                    pictures = uiState.user.profilePictures,
                    onPictureSelected = onAddPicture,
                    onRemovePicture = onRemovePicture
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.edit_name_title),
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.fillMaxWidth()
                    )
                    TextField(
                        value = uiState.user.name,
                        onValueChange = onChangeName,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
            item {
                val address = uiState.user.address
                EditProfileDescription(
                    description = uiState.user.description,
                    birthday = uiState.user.birthday.toString(DateTimeFormat.longDate()),
                    location = if (address.isUnknown()) stringResource(id = R.string.location_empty_title) else address.toText(),
                    onChangeDescription = onEditDescription,
                    onChangeBirthday = { showDatePicker = true },
                    onChangeLocation = onUpdateLocation,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            item {
                EditProfileLanguages(
                    languages = uiState.user.languages,
                    onRemoveLanguage = onRemoveLanguage,
                    onAddLanguage = onAddLanguage,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
            item {
                EditTopics(
                    selectedTopics = uiState.user.topics,
                    addTopic = onAddTopic,
                    removeTopic = onRemoveTopic,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .padding(horizontal = 24.dp)
                )
            }
        }

        if (showDatePicker) {
            BirthdaySelector(
                onSelect = {
                    onSwitchBirthday(it)
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false },
                currentSelected = uiState.user.birthday.millis
            )
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
    val state = rememberDatePickerState(initialSelectedDateMillis = currentSelected)

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
                Text(text = stringResource(id = R.string.select_new_birthday))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = stringResource(id = R.string.cancel_birthday_selection))
            }
        }
    ) {
        DatePicker(state = state)
    }
}