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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.compose.LazyPagingItems
import coil.compose.AsyncImage
import com.colledk.home.R
import com.colledk.home.domain.model.Post
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.User

@Composable
internal fun HomeFeed(
    posts: LazyPagingItems<Post>,
    onReportPost: (post: Post) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(24.dp)
    ) {
        items(posts.itemCount) { index ->
            posts[index]?.let {
                PostItem(
                    post = it,
                    onReportClicked = {
                        onReportPost(it)
                    }
                )
            }
        }
    }
}

@Composable
private fun PostItem(
    post: Post,
    onReportClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isExpanded by rememberSaveable(post) {
        mutableStateOf(false)
    }

    Card(
        modifier = if (isExpanded) modifier.animateContentSize() else modifier
            .height(160.dp)
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            PostItemHeader(user = post.user, onReportClicked = onReportClicked)
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = if (!isExpanded) Modifier.weight(1f) else Modifier
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            PostItemInformation(
                post = post,
                onLikeClicked = { /*TODO*/ },
                onCommentsClicked = { /*TODO*/ },
                onShareClicked = { /*TODO*/ })
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
        horizontalArrangement = Arrangement.spacedBy(4.dp)
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
    onLikeClicked: () -> Unit,
    onCommentsClicked: () -> Unit,
    onShareClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.clickable { onLikeClicked() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.like),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "${post.likes}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
        Row(
            modifier = Modifier.clickable { onCommentsClicked() }
        ) {
            Icon(
                painter = painterResource(id = R.drawable.comment), contentDescription = null,
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