package com.colledk.profile.ui.compose

import androidx.activity.compose.BackHandler
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

    LaunchedEffect(key1 = userId) {
        viewModel.getUser(userId = userId)
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<User>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = modifier,
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
                        onSave = editProfileViewModel::saveUser,
                        onAddPicture = { /*TODO*/ },
                        onRemovePicture = { /*TODO*/ },
                        onEditDescription = { /*TODO*/ },
                        onSwitchBirthday = { /*TODO*/ },
                        onUpdateLocation = { /*TODO*/ },
                        onAddLanguage = { /*TODO*/ },
                        onRemoveLanguage = { /*TODO*/ },
                        onAddTopic = { /*TODO*/ },
                        onRemoveTopic = { /*TODO*/ },
                        onChangeName = editProfileViewModel::updateName
                    )
                }
            }
        }
    )
}