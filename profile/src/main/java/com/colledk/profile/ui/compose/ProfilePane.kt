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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import coil.compose.AsyncImage
import com.colledk.profile.R
import com.colledk.profile.ui.uistates.ProfileUiState
import com.colledk.user.domain.model.Topic
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
    onCreateChat: (userId: String) -> Unit,
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
                        } else {
                            IconButton(onClick = { onCreateChat(uiState.currentUser.id) }) {
                                Icon(
                                    painter = painterResource(id = R.drawable.add_to_chat),
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
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
                            isInEditMode = false,
                            pictures = uiState.currentUser.profilePictures
                        )
                    }
                    item {
                        val location = uiState.currentUser.location
                        ProfileDescription(
                            description = uiState.currentUser.description,
                            birthday = uiState.currentUser.birthday.toString(DateTimeFormat.longDate()),
                            location = if (location.isUnknown()) stringResource(id = R.string.location_empty_title) else location.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }
                    item {
                        ProfileLanguages(
                            languages = uiState.currentUser.languages,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 24.dp)
                        )
                    }
                    item {
                        ProfileTopics(
                            topics = uiState.currentUser.topics,
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

private fun DateTime.getAge(): String {
    val now = DateTime.now()
    val period = Period(this, now, PeriodType.yearMonthDay())
    return period.years.toString()
}