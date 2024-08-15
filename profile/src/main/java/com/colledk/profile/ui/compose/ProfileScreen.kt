package com.colledk.profile.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.colledk.profile.ui.EditProfileViewModel
import com.colledk.profile.ui.ProfileViewModel
import com.colledk.profile.ui.uistates.EditProfileUiState
import com.colledk.profile.ui.uistates.ProfileUiState
import com.colledk.user.domain.model.User

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun ProfileScreen(
    userId: String,
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = hiltViewModel(),
    editProfileViewModel: EditProfileViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val refreshPage by editProfileViewModel.refreshPage.collectAsState(null)
    val errors by editProfileViewModel.error.collectAsState(null)

    val navigator = rememberListDetailPaneScaffoldNavigator<User>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    LaunchedEffect(key1 = refreshPage) {
        if (refreshPage != null) {
            viewModel.getUser(userId = userId).also {
                if (navigator.canNavigateBack()) {
                    navigator.navigateBack()
                } else {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, null)
                }
                snackbarHostState.showSnackbar("User updated!")
            }
        }
    }

    LaunchedEffect(key1 = errors) {
        if (errors != null) {
            snackbarHostState.showSnackbar(errors?.message ?: "")
        }
    }

    LaunchedEffect(key1 = userId) {
        viewModel.getUser(userId = userId)
    }



    Scaffold(
        modifier = modifier,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) {
        ListDetailPaneScaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
            directive = navigator.scaffoldDirective,
            value = navigator.scaffoldValue,
            listPane = {
                AnimatedPane {
                    ProfilePane(
                        isEditable = true,
                        uiState = uiState,
                        onEditProfile = {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                content = (uiState as ProfileUiState.Data).currentUser
                            )
                        },
                        onCreateChat = {}
                    )
                }
            },
            detailPane = {
                AnimatedPane {
                    navigator.currentDestination?.content?.let {
                        val editState by editProfileViewModel.currentState.collectAsState()

                        LaunchedEffect(key1 = it) {
                            editProfileViewModel.setUser(it)
                        }

                        EditProfilePane(
                            uiState = editState,
                            onSave = { editProfileViewModel.saveUser(editState.user) },
                            onAddPicture = { editProfileViewModel.updatePictures(editState.user.profilePictures.plus(it)) },
                            onRemovePicture = { editProfileViewModel.updatePictures(editState.user.profilePictures.minus(it)) },
                            onEditDescription = editProfileViewModel::updateDescription,
                            onSwitchBirthday = editProfileViewModel::updateBirthday,
                            onUpdateLocation = editProfileViewModel::getLocation,
                            onAddLanguage = { editProfileViewModel.updateLanguages(editState.user.languages.plus(it)) },
                            onRemoveLanguage = { editProfileViewModel.updateLanguages(editState.user.languages.minus(it)) },
                            onAddTopic = { editProfileViewModel.updateTopics(editState.user.topics.plus(it)) },
                            onRemoveTopic = { editProfileViewModel.updateTopics(editState.user.topics.minus(it)) },
                            onChangeName = editProfileViewModel::updateName
                        )
                    }
                }
            }
        )
    }
}