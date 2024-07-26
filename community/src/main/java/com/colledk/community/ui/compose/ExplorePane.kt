package com.colledk.community.ui.compose

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.colledk.chat.domain.model.AiItem
import com.colledk.community.R
import com.colledk.profile.ui.compose.ProfilePane
import com.colledk.profile.ui.uistates.ProfileUiState
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.LanguageProficiency
import com.colledk.user.domain.model.User
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
internal fun ExplorePane(
    users: LazyPagingItems<User>,
    onCreateAiChat: (ai: AiItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<User>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            Scaffold(
                topBar = {
                    ExploreTopBar(onSettingsClicked = {  })
                }
            ) { padding ->
                AnimatedPane {
                    val pagerState = rememberPagerState { 2 }
                    val scope = rememberCoroutineScope()

                    Column(
                        modifier = Modifier
                            .padding(padding)
                            .padding(top = 12.dp)
                    ) {
                        Row {
                            PagerIndicatorItem(
                                iconId = R.drawable.community,
                                titleId = R.string.community_page_title,
                                isSelected = pagerState.currentPage == 0,
                                modifier = Modifier
                                    .width(IntrinsicSize.Max)
                                    .weight(1f)
                            ) {
                                scope.launch {
                                    pagerState.animateScrollToPage(0)
                                }
                            }
                            PagerIndicatorItem(
                                iconId = R.drawable.robot,
                                titleId = R.string.chatbot_page_title,
                                isSelected = pagerState.currentPage == 1,
                                modifier = Modifier
                                    .width(IntrinsicSize.Max)
                                    .weight(1f)
                            ) {
                                scope.launch {
                                    pagerState.animateScrollToPage(1)
                                }
                            }
                        }
                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface)
                        HorizontalPager(state = pagerState) { page ->
                            when (page) {
                                0 -> {
                                    ExplorePaneContent(
                                        users = users,
                                        modifier = Modifier.fillMaxSize(),
                                        onUserClicked = {
                                            navigator.navigateTo(
                                                pane = ListDetailPaneScaffoldRole.Detail,
                                                content = it
                                            )
                                        }
                                    )
                                }
                                1 -> {
                                    ChatBotsPane(onCreateAiChat = onCreateAiChat, modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { user ->
                    val uiState = remember(user) { ProfileUiState.Data(user) }

                    ProfilePane(
                        isEditable = false,
                        uiState = uiState,
                        onEditProfile = {},
                        onPictureAdded = {},
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
    )
}

@Composable
private fun PagerIndicatorItem(
    @DrawableRes iconId: Int,
    @StringRes titleId: Int,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onIndicatorClicked: () -> Unit
) {
    Column(
        modifier = modifier.clickable { onIndicatorClicked() },
        verticalArrangement = Arrangement.spacedBy(2.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val color =
            if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        Icon(
            painter = painterResource(id = iconId),
            contentDescription = null,
            modifier = Modifier.size(24.dp),
            tint = color
        )
        Text(
            text = stringResource(id = titleId),
            color = color,
            style = MaterialTheme.typography.labelMedium
        )
        if (isSelected) {
            HorizontalDivider(color = color, thickness = 3.dp)
        }
    }
}

@Composable
private fun ExplorePaneContent(
    users: LazyPagingItems<User>,
    modifier: Modifier = Modifier,
    onUserClicked: (User) -> Unit
) {
    if (users.loadState.prepend == LoadState.Loading) {
        LoadingScreen(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        )
    } else {
        LazyColumn(
            modifier = modifier,
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(users.itemCount) {
                val user by remember {
                    mutableStateOf(users[it])
                }
                user?.let {
                    UserItem(
                        user = it,
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        onUserClicked(it)
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        repeat(10) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clip(CardDefaults.shape)
                    .shimmerEffect(color = MaterialTheme.colorScheme.secondaryContainer)
            )
        }
    }
}

@Composable
private fun UserItem(
    user: User,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier.height(140.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.weight(1f)
            ) {
                ProfilePicture(profilePicture = user.profilePictures.firstOrNull())
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = user.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = user.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                val languageSpoken by remember {
                    derivedStateOf {
                        user.languages.filter { it.proficiency == LanguageProficiency.FLUENT }
                            .map { it.language }
                    }
                }

                val languageLearning by remember {
                    derivedStateOf {
                        user.languages.filter { it.proficiency != LanguageProficiency.FLUENT }
                            .map { it.language }
                    }
                }

                LanguagesItem(
                    textId = R.string.community_user_speaks,
                    languages = languageSpoken,
                    modifier = Modifier.weight(1f)
                )
                LanguagesItem(
                    textId = R.string.community_user_learns,
                    languages = languageLearning,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun ProfilePicture(
    profilePicture: Uri?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(48.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
    ) {
        profilePicture?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = debugPlaceholder(),
                modifier = Modifier.fillMaxSize()
            )
        } ?: run {
            Icon(
                painter = painterResource(id = R.drawable.question_mark),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                tint = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    }
}

@Composable
private fun LanguagesItem(
    @StringRes textId: Int,
    languages: List<Locale>,
    modifier: Modifier = Modifier
) {
    val showPlus by remember {
        derivedStateOf {
            languages.size > 3
        }
    }

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(id = textId),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold
        )
        languages.take(if (showPlus) 2 else 3).fastForEach {
            Text(
                text = it.language.uppercase(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
                    .padding(4.dp)
            )
        }
        if (showPlus) {
            Text(
                text = "+${languages.size - 2}",
                color = MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
                    .padding(4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreTopBar(
    onSettingsClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = R.string.explore_page_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            IconButton(onClick = onSettingsClicked) {
                Icon(
                    painter = painterResource(id = R.drawable.options),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        modifier = modifier
    )
}