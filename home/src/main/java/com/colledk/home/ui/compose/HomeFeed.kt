package com.colledk.home.ui.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.colledk.home.R
import com.colledk.home.domain.model.Post
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun HomeFeed(
    userId: String,
    posts: LazyPagingItems<Post>,
    listState: LazyListState,
    onReportPost: (post: Post) -> Unit,
    onRefresh: () -> Unit,
    refreshState: PullToRefreshState,
    isRefreshing: Boolean,
    onLikePost: (post: Post) -> Unit,
    onRemoveLike: (post: Post) -> Unit,
    modifier: Modifier = Modifier
) {
    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = onRefresh,
        state = refreshState,
        modifier = modifier
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(24.dp),
            state = listState,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!isRefreshing) {
                items(posts.itemCount) { index ->
                    posts[index]?.let {
                        PostItem(
                            post = it,
                            onReportClicked = {
                                onReportPost(it)
                            },
                            userId = userId,
                            onLike = { onLikePost(it) },
                            onRemoveLike = { onRemoveLike(it) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PostItem(
    post: Post,
    userId: String,
    onLike: () -> Unit,
    onRemoveLike: () -> Unit,
    onReportClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable(post) {
        mutableStateOf(false)
    }

    var hasOverflow by remember {
        mutableStateOf(false)
    }

    Card(
        modifier = if (isExpanded) modifier.animateContentSize() else modifier
            .height(200.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            PostItemHeader(user = post.user, onReportClicked = onReportClicked)
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = if (!isExpanded) Modifier.weight(1f) else Modifier,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { textLayoutResult: TextLayoutResult ->
                    if (textLayoutResult.hasVisualOverflow) {
                        hasOverflow = true
                    }
                }
            )
            if (hasOverflow) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = if (isExpanded) stringResource(id = R.string.post_show_less) else stringResource(
                        id = R.string.post_show_more
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { isExpanded = !isExpanded },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            PostItemInformation(
                post = post,
                onLike = onLike,
                onRemoveLike = onRemoveLike,
                onCommentsClicked = { /*TODO*/ },
                onShareClicked = { /*TODO*/ },
                userId = userId
            )
        }
    }
}

@Composable
private fun PostItemHeader(
    user: User,
    onReportClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.onSecondaryContainer)
        ) {
            user.profilePictures.firstOrNull()?.let {
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

        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            // TODO languages
        }
        IconButton(onClick = onReportClicked) {
            Icon(
                painter = painterResource(id = R.drawable.warning),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun PostItemInformation(
    post: Post,
    userId: String,
    onLike: () -> Unit,
    onRemoveLike: () -> Unit,
    onCommentsClicked: () -> Unit,
    onShareClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val usersLiked = remember(post.likes) {
        mutableStateListOf(*post.likes.toTypedArray())
    }

    val isLiked by remember(usersLiked.size) {
        mutableStateOf(usersLiked.contains(element = userId))
    }

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable {
                if (isLiked) {
                    onRemoveLike().also {
                        usersLiked.remove(userId)
                    }
                } else {
                    onLike().also {
                        usersLiked.add(userId)
                    }
                }
            },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                painter = painterResource(id = if (isLiked) R.drawable.liked else R.drawable.like),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${usersLiked.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row(
            modifier = Modifier.clickable { onCommentsClicked() },
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.comment),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${post.replies.size}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = onShareClicked) {
            Icon(
                painter = painterResource(id = R.drawable.share), contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}