package com.colledk.chat.ui.compose

import android.location.Address
import android.net.Uri
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.colledk.chat.R
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.chat.ui.uistates.ChatUiState
import com.colledk.common.shimmerEffect
import com.colledk.theme.ColingoTheme
import com.colledk.theme.PreviewAnnotations
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.Gender
import com.colledk.user.domain.model.User
import kotlinx.coroutines.launch
import org.joda.time.DateTime

@Composable
internal fun ChatPane(
    state: ChatUiState,
    selectedPage: Int?,
    selectPage: (Int) -> Unit,
    onCreateNewChat: () -> Unit,
    onChatSelected: (chat: Chat) -> Unit,
    onAiChatSelected: (chat: AiChat) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
//            ExtendedFloatingActionButton(
//                text = {
//                    Text(
//                        text = stringResource(id = R.string.chat_create_new),
//                        style = MaterialTheme.typography.labelLarge,
//                        color = MaterialTheme.colorScheme.onPrimaryContainer
//                    )
//                },
//                icon = {
//                    Icon(
//                        painter = painterResource(id = R.drawable.pen),
//                        contentDescription = null,
//                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
//                        modifier = Modifier.size(24.dp)
//                    )
//                },
//                onClick = onCreateNewChat,
//                expanded = listState.isScrollingUp()
//            )
        },
        topBar = {
            ChatTopBar()
        }
    ) {
        val pagerState = rememberPagerState(initialPage = selectedPage ?: 0) { 2 }
        LaunchedEffect(key1 = pagerState.currentPage) {
            selectPage(pagerState.currentPage)
        }

        val scope = rememberCoroutineScope()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            ChatPagerIndicators(
                onItemClicked = {
                    scope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                },
                selectedIndex = pagerState.currentPage
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { index ->
                when(state) {
                    is ChatUiState.Data -> {
                        when (index) {
                            0 -> {
                                LazyColumn(
                                    modifier = Modifier.fillMaxSize(),
                                    contentPadding = PaddingValues(24.dp),
                                    verticalArrangement = Arrangement.spacedBy(16.dp),
                                    state = listState
                                ) {
                                    items(state.chats, key = { it.id }) { chat ->
                                        ChatItem(
                                            chat = chat,
                                            onChatSelected = { onChatSelected(chat) },
                                            currentUser = state.currentUser
                                        )
                                    }
                                    item {
                                        if (state.chats.isEmpty()) {
                                            ChatsEmpty(modifier = Modifier.fillParentMaxSize())
                                        }
                                    }
                                }
                            }

                            1 -> {
                                AiChatPane(
                                    modifier = Modifier.fillMaxSize(),
                                    aiChats = state.aiChats,
                                    onChatClicked = onAiChatSelected
                                )
                            }
                        }
                    }
                    ChatUiState.Loading -> LoadingScreen(modifier = Modifier.padding(24.dp))
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
private fun ChatsEmpty(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = stringResource(id = R.string.chats_empty_title), style = MaterialTheme.typography.titleLarge, textAlign = TextAlign.Center)
        Text(text = stringResource(id = R.string.chats_empty_description), style = MaterialTheme.typography.bodyLarge, textAlign = TextAlign.Center)
    }
}

@Composable
private fun ChatPagerIndicators(
    onItemClicked: (index: Int) -> Unit,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        PagerIndicatorItem(
            iconId = R.drawable.community_users,
            titleId = R.string.members_page_title,
            isSelected = selectedIndex == 0,
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .weight(1f)
        ) {
            onItemClicked(0)
        }
        PagerIndicatorItem(
            iconId = R.drawable.robot,
            titleId = R.string.bots_page_title,
            isSelected = selectedIndex == 1,
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .weight(1f)
        ) {
            onItemClicked(1)
        }
    }
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
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
        if (isSelected) {
            HorizontalDivider(color = color, thickness = 3.dp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ChatTopBar(modifier: Modifier = Modifier) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = stringResource(id = R.string.chat_my_messages),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
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

@Composable
private fun ChatItem(
    currentUser: User,
    chat: Chat,
    modifier: Modifier = Modifier,
    onChatSelected: () -> Unit
) {
    Card(
        onClick = onChatSelected,
        modifier = modifier.height(140.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    ) {
        val latestMessage: Message? by remember(chat.messages) {
            derivedStateOf {
                chat.messages.lastOrNull()
            }
        }

        val latestSender: User? by remember(chat.messages) {
            derivedStateOf {
                val lastMessage = chat.messages.lastOrNull()
                chat.users.find { it.id == lastMessage?.id }
            }
        }

        val otherUser: User by remember {
            derivedStateOf {
                chat.users.first { it.id != currentUser.id }
            }
        }

        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
            ) {
                otherUser.profilePictures.firstOrNull().let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        placeholder = debugPlaceholder()
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = otherUser.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = latestMessage?.content ?: stringResource(id = R.string.chat_empty_title),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    overflow = TextOverflow.Ellipsis,
                    fontStyle = if (latestMessage?.content == null) FontStyle.Italic else FontStyle.Normal
                )
            }
            Column {
                Text(text = latestMessage?.time.orEmpty())
                // TODO add unread notification
            }
        }
    }
}

@PreviewAnnotations
@Composable
private fun ChatPanePreview() {
    val uiState by remember {
        mutableStateOf(
            ChatUiState.Data(
                chats = listOf(
                    Chat(
                        id = "123",
                        users = listOf(
                            User(
                                id = "12",
                                name = "Johnny",
                                birthday = DateTime.now(),
                                profilePictures = emptyList(),
                                description = "",
                                address = Address(Locale.current.platformLocale),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                chats = listOf("123")
                            ),
                            User(
                                id = "13",
                                name = "Benny",
                                birthday = DateTime.now(),
                                profilePictures = listOf(Uri.parse("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg")),
                                description = "",
                                address = Address(Locale.current.platformLocale),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                chats = listOf("123")
                            )
                        ),
                        messages = listOf(
                            Message(
                                id = "123",
                                sender = User(
                                    id = "13",
                                    name = "Benny",
                                    birthday = DateTime.now(),
                                    profilePictures = emptyList(),
                                    description = "",
                                    address = Address(Locale.current.platformLocale),
                                    languages = emptyList(),
                                    gender = Gender.MALE,
                                    chats = listOf("123")
                                ),
                                content = "Hi Johnny, I really love you big time my man",
                                time = "12:53",
                                timestamp = 0L,
                                date = ""
                            )
                        )
                    ),
                    Chat(
                        id = "123",
                        users = listOf(
                            User(
                                id = "12",
                                name = "Johnny",
                                birthday = DateTime.now(),
                                profilePictures = emptyList(),
                                description = "",
                                address = Address(Locale.current.platformLocale),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                chats = listOf("123")
                            ),
                            User(
                                id = "13",
                                name = "Benny",
                                birthday = DateTime.now(),
                                profilePictures = listOf(Uri.parse("https://letsenhance.io/static/8f5e523ee6b2479e26ecc91b9c25261e/1015f/MainAfter.jpg")),
                                description = "",
                                address = Address(Locale.current.platformLocale),
                                languages = emptyList(),
                                gender = Gender.MALE,
                                chats = listOf("123")
                            )
                        ),
                        messages = listOf(
                            Message(
                                id = "123",
                                sender = User(
                                    id = "13",
                                    name = "Benny",
                                    birthday = DateTime.now(),
                                    profilePictures = emptyList(),
                                    description = "",
                                    address = Address(Locale.current.platformLocale),
                                    languages = emptyList(),
                                    gender = Gender.MALE,
                                    chats = listOf("123")
                                ),
                                content = "Hi Johnny, I really love you big time my man",
                                time = "12:53",
                                timestamp = 0L,
                                date = ""
                            )
                        )
                    )
                )
            )
        )
    }

    ColingoTheme {
        Surface(color = MaterialTheme.colorScheme.surface) {
            ChatPane(
                state = uiState,
                onCreateNewChat = {},
                onChatSelected = {},
                onAiChatSelected = {},
                selectedPage = null,
                selectPage = {}
            )
        }
    }
}