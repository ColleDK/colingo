package com.colledk.home.ui.compose

import android.net.Uri
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.colledk.home.R
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.model.Reply
import com.colledk.home.ui.PostViewModel
import com.colledk.home.ui.uistates.PostUiState
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.User

@Composable
internal fun PostPane(
    user: User,
    postId: String,
    modifier: Modifier = Modifier,
    viewModel: PostViewModel = hiltViewModel()
) {

    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(key1 = postId) {
        viewModel.retrievePost(postId)
    }

    when (state) {
        is PostUiState.Data -> {
            (state as PostUiState.Data).let { state ->
                PostPane(
                    userId = user.id,
                    post = state.post,
                    formatNumber = viewModel::formatNumber,
                    onLikePost = { viewModel.likePost(state.post, user.id) },
                    onRemoveLike = { viewModel.removeLike(state.post, user.id) },
                    onCreateReply = {
                        viewModel.addReply(
                            postId = state.post.id,
                            reply = it,
                            currentUser = user
                        )
                    },
                    modifier = modifier
                )
            }
        }

        PostUiState.Loading -> {

        }
    }

}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun PostPane(
    userId: String,
    post: Post,
    formatNumber: (num: Number) -> String,
    onLikePost: () -> Unit,
    onRemoveLike: () -> Unit,
    onCreateReply: (reply: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val usersLiked = remember(post.likes) {
        mutableStateListOf(*post.likes.toTypedArray())
    }

    val isLiked by remember(usersLiked.size) {
        mutableStateOf(usersLiked.contains(element = userId))
    }

    var showBottomSheet by remember {
        mutableStateOf(false)
    }

    Scaffold(
        modifier = modifier,
        bottomBar = {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .clickable {
                        showBottomSheet = true
                    },
            ) {
                Text(text = "Add a comment", modifier = Modifier.padding(12.dp))
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                PostContent(
                    postText = post.content,
                    user = post.user,
                    modifier = Modifier.fillParentMaxWidth()
                )
            }
            stickyHeader {
                Surface(modifier = Modifier.fillParentMaxWidth()) {
                    PostDetailHeader(
                        likes = formatNumber(usersLiked.size) + " likes",
                        comments = formatNumber(post.replies.size) + " comments",
                        modifier = Modifier.fillParentMaxWidth(),
                        isPostLiked = isLiked,
                        onLikeClicked = {
                            if (isLiked) {
                                onRemoveLike().also {
                                    usersLiked.remove(userId)
                                }
                            } else {
                                onLikePost().also {
                                    usersLiked.add(userId)
                                }
                            }
                        }
                    )
                }
            }
            item {
                HorizontalDivider(thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface)
            }
            items(post.replies) {
                ReplyItem(reply = it, modifier = Modifier.fillParentMaxWidth())
            }
            if (post.replies.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier.fillParentMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.empty),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillParentMaxWidth(.4f)
                        )
                        Text(
                            text = "No replies yet.\nBe the first to share your thoughts on this post!",
                            style = MaterialTheme.typography.headlineSmall,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

        if (showBottomSheet) {
            CreateReplyBottomSheet(
                sheetState = sheetState,
                onDismiss = { showBottomSheet = false },
                onCreateReply = {
                    onCreateReply(it).also {
                        showBottomSheet = false
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateReplyBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onCreateReply: (reply: String) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        sheetState = sheetState,
        modifier = modifier,
        onDismissRequest = onDismiss
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            var currentText by remember {
                mutableStateOf("")
            }

            TextField(
                value = currentText,
                onValueChange = { currentText = it },
                modifier = Modifier.weight(1f)
            )
            IconButton(
                onClick = { onCreateReply(currentText) },
                enabled = currentText.isNotBlank()
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.reply),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
private fun PostContent(
    postText: String,
    user: User,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            ProfilePicture(picture = user.profilePictures.firstOrNull())
            Text(text = user.name)
        }
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            )
        ) {
            Text(
                modifier = Modifier.padding(12.dp),
                text = postText,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun ProfilePicture(
    picture: Uri?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(MaterialTheme.colorScheme.onSecondaryContainer)
    ) {
        picture?.let {
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
private fun PostDetailHeader(
    isPostLiked: Boolean,
    likes: String,
    comments: String,
    onLikeClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(2) {
            val isLikes = it == 0
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .then(
                        if (isPostLiked && isLikes) Modifier.background(MaterialTheme.colorScheme.secondary) else Modifier
                    )
                    .then(
                        if (isLikes) Modifier.clickable { onLikeClicked() } else Modifier
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outline,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(8.dp)
                    .height(IntrinsicSize.Min)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val contentColor =
                        if (isPostLiked && isLikes) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface

                    Icon(
                        painter = painterResource(id = if (isLikes) R.drawable.like else R.drawable.comment),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = contentColor
                    )
                    VerticalDivider(
                        thickness = 1.dp,
                        color = contentColor
                    )
                    Text(
                        text = if (isLikes) likes else comments,
                        color = contentColor
                    )
                }
            }
        }
    }
}

@Composable
private fun ReplyItem(
    reply: Reply,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfilePicture(picture = reply.user.profilePictures.firstOrNull(), modifier = Modifier.size(24.dp))
                Text(text = reply.user.name, style = MaterialTheme.typography.labelLarge)
            }
            Text(text = reply.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}