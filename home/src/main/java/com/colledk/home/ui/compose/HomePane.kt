package com.colledk.home.ui.compose

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.paging.LoadState
import com.colledk.home.R
import com.colledk.home.domain.model.Post
import com.colledk.home.ui.uistates.HomeUiState
import com.colledk.profile.ui.compose.ProfilePane
import com.colledk.profile.ui.uistates.ProfileUiState
import com.colledk.user.domain.model.Topic
import com.google.firebase.firestore.Query.Direction

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class)
@Composable
internal fun HomePane(
    uiState: HomeUiState,
    onLikePost: (post: Post) -> Unit,
    onRemoveLike: (post: Post) -> Unit,
    onRefresh: () -> Unit,
    onCreatePost: (text: String, topics: List<Topic>) -> Unit,
    onSort: (sorting: Direction) -> Unit,
    formatNumber: (num: Number) -> String,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val refreshState = rememberPullToRefreshState()
    var initialLoad by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(key1 = uiState.posts.loadState.refresh) {
        if (uiState.posts.loadState.refresh != LoadState.Loading) {
            initialLoad = false
        }
    }

    DisposableEffect(key1 = Unit) {
        onDispose {
            initialLoad = true
        }
    }

    var showCreatePost by remember {
        mutableStateOf(false)
    }

    val navigator = rememberListDetailPaneScaffoldNavigator<HomeDetailDestination>()

    BackHandler(navigator.canNavigateBack()) {
        navigator.navigateBack()
    }
    ListDetailPaneScaffold(
        modifier = modifier,
        directive = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        listPane = {
            AnimatedPane {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        HomeTopBar(
                            currentSorting = uiState.currentSort
                        ) {
                            onSort(it).also {
                                onRefresh()
                            }
                        }
                    },
                    floatingActionButton = {
                        ExtendedFloatingActionButton(
                            text = {
                                Text(
                                    text = stringResource(id = R.string.post_create_new),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.pen),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                    modifier = Modifier.size(24.dp)
                                )
                            },
                            onClick = { showCreatePost = true },
                            expanded = listState.isScrollingUp()
                        )
                    }
                ) { padding ->
                    HomeFeed(
                        posts = uiState.posts,
                        listState = listState,
                        onReportPost = { /* TODOD */ },
                        refreshState = refreshState,
                        isRefreshing = uiState.posts.loadState.refresh == LoadState.Loading && !initialLoad,
                        onRefresh = onRefresh,
                        modifier = Modifier.padding(padding),
                        onLikePost = onLikePost,
                        onRemoveLike = onRemoveLike,
                        userId = uiState.currentUser.id,
                        onProfileClicked = {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                content = HomeDetailDestination.ProfileDestination(it)
                            )
                        },
                        onPostClicked = {
                            navigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                                content = HomeDetailDestination.PostDestination(it.id)
                            )
                        },
                        formatNumber = formatNumber
                    )
                }
                if (showCreatePost) {
                    CreateNewPost(
                        onDismiss = { showCreatePost = false },
                        onCreate = { text, topics ->
                            showCreatePost = false
                            onCreatePost(text, topics)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                navigator.currentDestination?.content?.let { destination ->
                    when(destination) {
                        is HomeDetailDestination.PostDestination -> {
                            PostPane(user = uiState.currentUser, postId = destination.postId)
                        }
                        is HomeDetailDestination.ProfileDestination -> {
                            val profileState by remember(destination.user) {
                                mutableStateOf(ProfileUiState.Data(destination.user))
                            }

                            ProfilePane(
                                isEditable = false,
                                uiState = profileState,
                                onEditProfile = {},
                                onPictureAdded = {}
                            )
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun LazyListState.isScrollingUp(): Boolean {
    var previousIndex by remember(this) {
        mutableIntStateOf(firstVisibleItemIndex)
    }

    var previousScrollOffset by remember(this) {
        mutableIntStateOf(firstVisibleItemScrollOffset)
    }

    return remember(this) {
        derivedStateOf {
            if (previousIndex != firstVisibleItemIndex) {
                previousIndex > firstVisibleItemIndex
            } else {
                previousScrollOffset >= firstVisibleItemScrollOffset
            }.also {
                previousIndex = firstVisibleItemIndex
                previousScrollOffset = firstVisibleItemScrollOffset
            }
        }
    }.value
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(
    currentSorting: Direction,
    modifier: Modifier = Modifier,
    onSort: (sorting: Direction) -> Unit
) {
    var isDropdownShown by remember {
        mutableStateOf(false)
    }

    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.homepage_title),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        actions = {
            Box {
                IconButton(onClick = { isDropdownShown = !isDropdownShown }) {
                    Icon(
                        painter = painterResource(id = R.drawable.sorting),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                HomeFeedSorting(
                    isOpen = isDropdownShown,
                    onDismiss = { isDropdownShown = false },
                    onSelect = {
                        onSort(it).also {
                            isDropdownShown = false
                        }
                    },
                    currentSorting = currentSorting
                )
            }
        }
    )
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CreateNewPost(
    onDismiss: () -> Unit,
    onCreate: (text: String, topics: List<Topic>) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(modifier = modifier) {
            var text by remember {
                mutableStateOf("")
            }

            val selectedTopics = remember {
                mutableStateListOf<Topic>()
            }

            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.post_create_new),
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = text,
                    onValueChange = { text = it },
                    modifier = Modifier.fillMaxWidth()
                )
                // TODO add topics?
                /*
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Topic.entries.fastForEach { topic ->
                            TopicItem(
                                topic = topic,
                                isSelected = selectedTopics.contains(topic),
                                onSelect = {
                                    if (selectedTopics.contains(topic)) {
                                        selectedTopics.remove(topic)
                                    } else {
                                        selectedTopics.add(topic)
                                    }
                                }
                            )
                        }
                    }
                 */
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    TextButton(
                        onClick = onDismiss,
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.create_post_dismiss))
                    }
                    TextButton(
                        onClick = { onCreate(text, selectedTopics) },
                        modifier = Modifier.padding(8.dp),
                    ) {
                        Text(stringResource(id = R.string.create_post_create))
                    }
                }
            }
        }
    }
}

@Composable
private fun TopicItem(
    topic: Topic,
    isSelected: Boolean,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clip(RoundedCornerShape(4.dp))
            .background(if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.secondaryContainer)
            .padding(4.dp)
            .sizeIn(minWidth = 24.dp, minHeight = 24.dp)
            .toggleable(
                value = isSelected,
                onValueChange = {
                    onSelect()
                }
            ),
        verticalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterVertically)
    ) {
        Icon(
            painter = painterResource(id = topic.icon),
            contentDescription = null,
            tint = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(id = topic.topicName),
            color = if (isSelected) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.onSecondaryContainer,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun HomeFeedSorting(
    currentSorting: Direction,
    isOpen: Boolean,
    onDismiss: () -> Unit,
    onSelect: (direction: Direction) -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = isOpen,
        onDismissRequest = onDismiss,
        modifier = modifier
    ) {
        repeat(Direction.entries.size) {
            val entry = Direction.entries[it]
            DropdownMenuItem(
                text = {
                    val text = when(entry) {
                        Direction.ASCENDING -> "Oldest"
                        Direction.DESCENDING -> "Newest"
                    }
                    Text(text = text)
                },
                trailingIcon = {
                    if (entry == currentSorting) {
                        Icon(
                            painter = painterResource(id = R.drawable.checkmark),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                onClick = {
                    onSelect(entry)
                }
            )
        }
    }
}