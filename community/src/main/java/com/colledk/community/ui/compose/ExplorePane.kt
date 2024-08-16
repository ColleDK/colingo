package com.colledk.community.ui.compose

import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.DefaultAlpha
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
import timber.log.Timber
import java.util.Locale

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun ExplorePane(
    userLanguages: List<String>,
    currentFilters: List<String>,
    users: LazyPagingItems<User>,
    onCreateAiChat: (ai: AiItem) -> Unit,
    selectFilters: (codes: List<String>) -> Unit,
    modifier: Modifier = Modifier
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<User>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            Scaffold(
                topBar = {
                    ExploreTopBar(
                        onSettingsClicked = { showBottomSheet = !showBottomSheet },
                        filterCount = currentFilters.size
                    )
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
                                    ChatBotsPane(
                                        onCreateAiChat = onCreateAiChat,
                                        currentFilters = currentFilters,
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (showBottomSheet) {
                val filters = remember(currentFilters) {
                    mutableStateListOf(*currentFilters.toTypedArray())
                }

                SelectFiltersBottomSheet(
                    currentFilters = filters,
                    sheetState = sheetState,
                    onDismiss = {
                        showBottomSheet = false
                        selectFilters(filters)
                    },
                    onSelectFilter = {
                        if (filters.contains(it)) filters.remove(it) else filters.add(it)
                    },
                    clearFilters = filters::clear,
                    userLanguages = userLanguages,
                    modifier = Modifier.fillMaxWidth()
                )
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
                        onCreateChat = {},
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
            items(users.itemCount, key = { users[it]?.id.orEmpty() }) {
                users[it]?.let { user ->
                    UserItem(
                        user = user,
                        modifier = Modifier.fillParentMaxWidth()
                    ) {
                        onUserClicked(user)
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
    filterCount: Int,
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
            BadgedBox(
                badge = {
                    if (filterCount > 0) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.onSurface,
                            contentColor = MaterialTheme.colorScheme.surface
                        ) {
                            Text(text = "$filterCount")
                        }
                    }
                }
            ) {
                IconButton(onClick = onSettingsClicked) {
                    Icon(
                        painter = painterResource(id = R.drawable.options),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun SelectFiltersBottomSheet(
    userLanguages: List<String>,
    currentFilters: List<String>,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onSelectFilter: (code: String) -> Unit,
    clearFilters: () -> Unit,
    modifier: Modifier = Modifier,
    limit: Int = 20
) {
    ModalBottomSheet(
        sheetState = sheetState,
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        val allLanguages by remember {
            derivedStateOf {
                Locale.getAvailableLocales()
                    .filter { it.displayLanguage.isNotBlank() }
                    .distinctBy { it.displayLanguage }.sortedBy { it.displayLanguage }
            }
        }

        val userFilters by remember {
            derivedStateOf {
                allLanguages.filter { userLanguages.contains(it.language) }
            }
        }
        val remainingFilters by remember {
            derivedStateOf {
                allLanguages.filterNot { userLanguages.contains(it.language) }
                    .groupBy { it.displayLanguage.first() }
            }
        }

        Column(
            Modifier.padding(horizontal = 24.dp)
        ) {
            Text(text = "Choose up to $limit languages you want to see fluent speakers from!")
            if (currentFilters.isNotEmpty()) {
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${currentFilters.size}/$limit selected",
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = clearFilters) {
                        Text(text = "Clear filters")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                stickyHeader {
                    Surface(
                        modifier = Modifier.fillParentMaxWidth(),
                        color = MaterialTheme.colorScheme.surfaceContainerLow
                    ) {
                        Text(text = "Your current languages", fontWeight = FontWeight.Bold)
                    }
                }
                items(userFilters) { locale ->
                    val isSelected by remember {
                        derivedStateOf { currentFilters.contains(locale.language) }
                    }

                    ListItem(
                        headlineContent = {
                            Text(text = locale.displayLanguage)
                        },
                        leadingContent = {
                            Checkbox(
                                checked = isSelected,
                                onCheckedChange = null
                            )
                        },
                        modifier = Modifier
                            .alpha(if (currentFilters.size < limit || isSelected) 1f else 0.38f)
                            .selectable(
                                selected = isSelected,
                                enabled = currentFilters.size < limit || isSelected
                            ) {
                                onSelectFilter(locale.language)
                            }
                    )
                }
                remainingFilters.forEach { (startLetter, locales) ->
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillParentMaxWidth(),
                            color = MaterialTheme.colorScheme.surfaceContainerLow
                        ) {
                            Text(text = "$startLetter", fontWeight = FontWeight.Bold)
                        }
                    }
                    items(locales) { locale ->
                        val isSelected by remember {
                            derivedStateOf { currentFilters.contains(locale.language) }
                        }

                        ListItem(
                            headlineContent = {
                                Text(text = locale.displayLanguage)
                            },
                            leadingContent = {
                                Checkbox(
                                    checked = isSelected,
                                    onCheckedChange = null
                                )
                            },
                            modifier = Modifier
                                .alpha(if (currentFilters.size < limit || isSelected) 1f else 0.38f)
                                .selectable(
                                    selected = isSelected,
                                    enabled = currentFilters.size < limit || isSelected
                                ) {
                                    onSelectFilter(locale.language)
                                }
                        )
                    }
                }
            }
        }
    }
}