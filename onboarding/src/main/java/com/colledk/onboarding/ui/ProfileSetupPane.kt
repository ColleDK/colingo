package com.colledk.onboarding.ui

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.window.core.layout.WindowWidthSizeClass
import com.colledk.onboarding.R
import com.colledk.onboarding.ui.ProfileSetupDestination.ADD_DESCRIPTION
import com.colledk.onboarding.ui.ProfileSetupDestination.ADD_PICTURE
import com.colledk.onboarding.ui.ProfileSetupDestination.SELECT_GENDER
import com.colledk.onboarding.ui.ProfileSetupDestination.SELECT_LANGUAGES
import com.colledk.onboarding.ui.ProfileSetupDestination.SELECT_TOPICS
import com.colledk.onboarding.ui.profilesetup.AddDescriptionPane
import com.colledk.onboarding.ui.profilesetup.ProfileSetupViewModel
import com.colledk.onboarding.ui.profilesetup.SelectGender
import com.colledk.onboarding.ui.profilesetup.SelectLanguagesPane
import com.colledk.onboarding.ui.profilesetup.SelectPicturePane
import com.colledk.onboarding.ui.profilesetup.SelectTopicsPane
import com.colledk.onboarding.ui.profilesetup.uistates.DescriptionUiState
import com.colledk.onboarding.ui.profilesetup.uistates.GenderUiState
import com.colledk.onboarding.ui.profilesetup.uistates.LanguagesUiState
import com.colledk.onboarding.ui.profilesetup.uistates.ProfilePictureUiState
import com.colledk.onboarding.ui.profilesetup.uistates.TopicsUiState
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.UserLanguage
import kotlinx.coroutines.launch

@Composable
internal fun ProfileSetupPane(
    modifier: Modifier = Modifier,
    viewModel: ProfileSetupViewModel = hiltViewModel(),
    onFinishSetup: () -> Unit
) {
    val pictureUiState by viewModel.profilePictureState.collectAsState()
    val descriptionUiState by viewModel.descriptionState.collectAsState()
    val languageUiState by viewModel.languagesState.collectAsState()
    val topicsUiState by viewModel.topicState.collectAsState()
    val genderUiState by viewModel.genderState.collectAsState()

    ProfileSetupPane(
        pictureUiState = pictureUiState,
        descriptionUiState = descriptionUiState,
        languagesUiState = languageUiState,
        topicsUiState = topicsUiState,
        genderUiState = genderUiState,
        modifier = modifier,
        onPictureSelected = viewModel::updateProfilePicture,
        onPictureRemoved = viewModel::removeProfilePicture,
        updateBirthday = viewModel::updateBirthday,
        getLocation = viewModel::getLocation,
        updateDescription = viewModel::updateDescription,
        onAddLanguage = viewModel::addLanguage,
        onRemoveLanguage = viewModel::removeLanguage,
        addTopic = viewModel::selectTopic,
        removeTopic = viewModel::removeTopic,
        onGenderSelected = viewModel::selectGender,
        onFinishSetup = onFinishSetup
    )
}

@Composable
private fun ProfileSetupPane(
    pictureUiState: ProfilePictureUiState,
    descriptionUiState: DescriptionUiState,
    languagesUiState: LanguagesUiState,
    topicsUiState: TopicsUiState,
    genderUiState: GenderUiState,
    onPictureSelected: (uri: Uri?) -> Unit,
    onPictureRemoved: () -> Unit,
    updateBirthday: (time: Long) -> Unit,
    getLocation: () -> Unit,
    updateDescription: (description: String) -> Unit,
    onAddLanguage: (language: UserLanguage) -> Unit,
    onRemoveLanguage: (language: UserLanguage) -> Unit,
    addTopic: (topic: Topic) -> Unit,
    removeTopic: (topic: Topic) -> Unit,
    onGenderSelected: (gender: Gender) -> Unit,
    modifier: Modifier = Modifier,
    onFinishSetup: () -> Unit
) {
    Box(
        modifier = modifier.then(
            Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceTint,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
        )
    ) {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val pagesPerScreen =
            if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) 2 else 1

        val scope = rememberCoroutineScope()

        val pagerState = rememberPagerState { ProfileSetupDestination.entries.size }
        val pageSize = remember {
            object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    return ((availableSpace - (pagesPerScreen - 1) * pageSpacing) / pagesPerScreen)
                }
            }
        }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageSize = pageSize,
            contentPadding = PaddingValues(bottom = 92.dp, top = 48.dp)
        ) { page ->
            ProfileSetupPage(
                page = ProfileSetupDestination.entries[page],
                pictureUiState = pictureUiState,
                descriptionUiState = descriptionUiState,
                languagesUiState = languagesUiState,
                topicsUiState = topicsUiState,
                genderUiState = genderUiState,
                onPictureSelected = onPictureSelected,
                onPictureRemoved = onPictureRemoved,
                updateBirthday = updateBirthday,
                getLocation = getLocation,
                updateDescription = updateDescription,
                onAddLanguage = onAddLanguage,
                onRemoveLanguage = onRemoveLanguage,
                addTopic = addTopic,
                removeTopic = removeTopic,
                onGenderSelected = onGenderSelected,
                modifier = Modifier.fillMaxSize()
            )
        }

        val shouldShowSkipButton by remember {
            derivedStateOf {
                (pagerState.currentPage == pagerState.pageCount - 1 || (pagesPerScreen > 1 && pagerState.currentPage + 1 == pagerState.pageCount - 1)).not()
            }
        }
        if (shouldShowSkipButton) {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.pageCount)
                    }
                },
                modifier = Modifier.align(Alignment.TopEnd),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = stringResource(id = R.string.setup_profile_skip),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NextButton(
                btnText = if (shouldShowSkipButton) stringResource(id = R.string.setup_profile_next) else stringResource(id = R.string.setup_profile_finish),
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .heightIn(min = 48.dp)
            ) {
                if (shouldShowSkipButton) {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.currentPage + 1)
                    }
                } else {
                    onFinishSetup()
                }
            }
            Row {
                repeat(pagerState.pageCount) { index ->
                    val color =
                        if (pagerState.currentPage == index || (pagesPerScreen > 1 && pagerState.currentPage + 1 == index)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun NextButton(
    btnText: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(2.dp)
    ) {
        Text(
            text = btnText,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ProfileSetupPage(
    page: ProfileSetupDestination,
    pictureUiState: ProfilePictureUiState,
    descriptionUiState: DescriptionUiState,
    languagesUiState: LanguagesUiState,
    topicsUiState: TopicsUiState,
    genderUiState: GenderUiState,
    onPictureSelected: (uri: Uri?) -> Unit,
    onPictureRemoved: () -> Unit,
    updateBirthday: (time: Long) -> Unit,
    getLocation: () -> Unit,
    updateDescription: (description: String) -> Unit,
    onAddLanguage: (language: UserLanguage) -> Unit,
    onRemoveLanguage: (language: UserLanguage) -> Unit,
    addTopic: (topic: Topic) -> Unit,
    removeTopic: (topic: Topic) -> Unit,
    onGenderSelected: (gender: Gender) -> Unit,
    modifier: Modifier = Modifier
) {
    when (page) {
        ADD_PICTURE -> {
            SelectPicturePane(
                modifier = modifier,
                profilePicture = pictureUiState.profilePicture,
                onPictureSelected = onPictureSelected,
                onPictureRemoved = onPictureRemoved
            )
        }

        ADD_DESCRIPTION -> {
            AddDescriptionPane(
                birthday = descriptionUiState.birthday,
                location = descriptionUiState.location,
                description = descriptionUiState.description,
                updateBirthday = updateBirthday,
                getLocation = getLocation,
                updateDescription = updateDescription,
                modifier = modifier
            )
        }

        SELECT_LANGUAGES -> {
            SelectLanguagesPane(
                selectedLanguages = languagesUiState.languages,
                onAddLanguage = onAddLanguage,
                onRemoveLanguage = onRemoveLanguage,
                modifier = modifier
            )
        }

        SELECT_TOPICS -> {
            SelectTopicsPane(
                selectedTopics = topicsUiState.selectedTopics,
                addTopic = addTopic,
                removeTopic = removeTopic,
                modifier = modifier
            )
        }

        SELECT_GENDER -> {
            SelectGender(
                selectedGender = genderUiState.selectedGender,
                onGenderSelected = onGenderSelected,
                modifier = modifier
            )
        }
    }
}