package com.colledk.community.ui.compose

import android.net.Uri
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.colledk.community.R
import com.colledk.community.ui.ExploreViewModel
import com.colledk.theme.debugPlaceholder
import com.colledk.user.domain.model.Language
import com.colledk.user.domain.model.LanguageProficiency
import com.colledk.user.domain.model.User
import com.colledk.user.domain.model.UserLanguage

@Composable
internal fun ExplorePane(
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
                
                LanguagesItem(textId = R.string.community_user_speaks, languages = languageSpoken, modifier = Modifier.weight(1f))
                LanguagesItem(textId = R.string.community_user_learns, languages = languageLearning, modifier = Modifier.weight(1f))
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
    languages: List<Language>,
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
                text = it.code.uppercase(),
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