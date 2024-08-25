package com.colledk.profile.ui.compose

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.colledk.profile.R
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue

@Composable
internal fun ProfilePictures(
    isInEditMode: Boolean,
    pictures: List<Uri>,
    modifier: Modifier = Modifier,
    onRemovePicture: (uri: Uri) -> Unit = {},
    onPictureSelected: (uri: Uri) -> Unit = {}
) {
    val pagerState = rememberPagerState { if (isInEditMode) 5 else pictures.size.coerceAtLeast(1) }
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) { result ->
            result?.let {
                onPictureSelected(it).also {
                    scope.launch {
                        pagerState.animateScrollToPage(pictures.size)
                    }
                }
            }
        }

    Box(
        modifier = modifier
    ) {
        HorizontalPager(state = pagerState) { page ->
            Picture(
                isInEditMode = isInEditMode,
                picture = pictures.getOrNull(page),
                onAddPictureClicked = {
                    launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                },
                onRemovePicture = onRemovePicture,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .pagerFadeTransition(page = page, pagerState = pagerState)
            )
        }
        Indicators(
            pageCount = pagerState.pageCount,
            currentPage = pagerState.currentPage,
            modifier = Modifier
                .height(24.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun Picture(
    isInEditMode: Boolean,
    picture: Uri?,
    onAddPictureClicked: () -> Unit,
    onRemovePicture: (uri: Uri) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
    ) {
        picture?.let {
            AsyncImage(
                model = it,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize(),
            )

            if (isInEditMode) {
                IconButton(
                    onClick = { onRemovePicture(it) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(4.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        } ?: run {
            Icon(
                painter = painterResource(id = if (isInEditMode) R.drawable.add_picture else R.drawable.question_mark),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
                    .then(
                        if (isInEditMode) {
                            Modifier.clickable {
                                onAddPictureClicked()
                            }
                        } else {
                            Modifier
                        }
                    ),
                tint = MaterialTheme.colorScheme.onSecondaryContainer
            )
        }
    }
}

@Composable
private fun Indicators(
    pageCount: Int,
    currentPage: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
    ) {
        repeat(pageCount) { index ->
            val lineWeight =
                animateFloatAsState(
                    targetValue = if (currentPage == index) 1.5f else 1f,
                    label = "size",
                    animationSpec = tween(durationMillis = 300, easing = EaseInOut)
                )

            val color =
                if (currentPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(color)
                    .weight(lineWeight.value)
                    .height(3.dp)
            )
        }
    }
}

private fun Modifier.pagerFadeTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset = pagerState.getOffsetDistanceInPages(page)
        translationX = -pageOffset * size.width
        alpha = 1 - pageOffset.absoluteValue
    }