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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil.compose.AsyncImage
import com.colledk.profile.R
import com.colledk.profile.ui.uistates.ProfileUiState
import com.colledk.user.domain.model.UserLanguage
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.Period
import org.joda.time.PeriodType
import org.joda.time.format.DateTimeFormat
import kotlin.math.absoluteValue

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePane(
    isEditable: Boolean,
    uiState: ProfileUiState,
    onEditProfile: () -> Unit,
    onPictureAdded: (uri: Uri?) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            if (uiState is ProfileUiState.Data) {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = uiState.currentUser.name,
                                style = MaterialTheme.typography.headlineLarge,
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.alignByBaseline()
                            )
                            Text(
                                text = uiState.currentUser.birthday.getAge(),
                                style = MaterialTheme.typography.headlineSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.alignByBaseline()
                            )
                        }
                    },
                    actions = {
                        if (isEditable) {
                            IconButton(onClick = onEditProfile) {
                                Icon(
                                    painter = painterResource(id = R.drawable.edit),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    ) { padding ->
        when (uiState) {
            is ProfileUiState.Data -> {
                val listState = rememberLazyListState()

                LaunchedEffect(key1 = uiState.currentUser) {
                    listState.scrollToItem(0)
                }

                LazyColumn(
                    modifier = Modifier.padding(padding),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    item {
                        ProfilePictures(
                            isEditable = isEditable,
                            pictures = uiState.currentUser.profilePictures,
                            onPictureSelected = onPictureAdded
                        )
                    }
                    item {
                        Description(
                            description = uiState.currentUser.description,
                            birthday = uiState.currentUser.birthday.toString(DateTimeFormat.longDate()),
                            location = uiState.currentUser.location.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }
                    item {
                        Languages(
                            languages = uiState.currentUser.languages,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }
                }
            }

            is ProfileUiState.Loading -> {

            }
        }
    }
}

@Composable
private fun ProfilePictures(
    isEditable: Boolean,
    pictures: List<Uri>,
    onPictureSelected: (uri: Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    val pagerState = rememberPagerState { if (isEditable) 5 else pictures.size }
    val scope = rememberCoroutineScope()
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            onPictureSelected(it).also {
                scope.launch {
                    pagerState.animateScrollToPage(pictures.size)
                }
            }
        }


    Box(
        modifier = modifier
    ) {
        HorizontalPager(state = pagerState) { page ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
                    .pagerFadeTransition(page = page, pagerState = pagerState)
            ) {
                pictures.getOrNull(page)?.let {
                    AsyncImage(
                        model = it,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                } ?: run {
                    Icon(
                        painter = painterResource(id = R.drawable.add_picture),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 24.dp)
                            .clickable {
                                launcher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                            },
                        tint = MaterialTheme.colorScheme.onSecondaryContainer
                    )
                }
            }
        }
        Row(
            modifier = Modifier
                .height(24.dp)
                .padding(horizontal = 24.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            repeat(pagerState.pageCount) { index ->
                val lineWeight =
                    animateFloatAsState(
                        targetValue = if (pagerState.currentPage == index) 1.5f else 1f,
                        label = "size",
                        animationSpec = tween(durationMillis = 300, easing = EaseInOut)
                    )

                val color =
                    if (pagerState.currentPage == index) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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
}

@Composable
private fun Description(
    description: String,
    birthday: String,
    location: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Description",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        Card(
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 140.dp)
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.padding(12.dp)
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.birthday),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = birthday,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.location),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = location,
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun Languages(
    languages: List<UserLanguage>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Languages",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.fillMaxWidth()
        )
        languages.fastForEach { language ->
            UserLanguageItem(userLanguage = language, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun UserLanguageItem(
    userLanguage: UserLanguage,
    modifier: Modifier = Modifier
) {
    ListItem(
        headlineContent = {
            Text(
                text = userLanguage.language.displayName,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold
            )
        },
        leadingContent = {
            Text(
                text = userLanguage.language.language.uppercase(),
                color = MaterialTheme.colorScheme.secondaryContainer,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
                    .padding(4.dp)
            )
        },
        trailingContent = {
            Text(
                text = userLanguage.proficiency.name.lowercase().capitalize(Locale.current),
                style = MaterialTheme.typography.bodyMedium
            )
        },
        modifier = modifier.clip(RoundedCornerShape(4.dp)),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    )
}

fun Modifier.pagerFadeTransition(page: Int, pagerState: PagerState) =
    graphicsLayer {
        val pageOffset = pagerState.getOffsetDistanceInPages(page)
        translationX = -pageOffset * size.width
        alpha = 1 - pageOffset.absoluteValue
    }

private fun DateTime.getAge(): String {
    val now = DateTime.now()
    val period = Period(this, now, PeriodType.yearMonthDay())
    return period.years.toString()
}