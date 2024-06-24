package com.colledk.onboarding.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowWidthSizeClass
import com.colledk.onboarding.R
import com.colledk.onboarding.ui.ProfileSetupDestination.ADD_DESCRIPTION
import com.colledk.onboarding.ui.ProfileSetupDestination.ADD_PICTURE
import com.colledk.onboarding.ui.ProfileSetupDestination.SELECT_GENDER
import com.colledk.onboarding.ui.ProfileSetupDestination.SELECT_LANGUAGES
import com.colledk.onboarding.ui.ProfileSetupDestination.SELECT_TOPICS
import com.colledk.onboarding.ui.ProfileSetupDestination.entries
import com.colledk.onboarding.ui.profilesetup.AddDescriptionPane
import com.colledk.onboarding.ui.profilesetup.SelectGender
import com.colledk.onboarding.ui.profilesetup.SelectLanguagesPane
import com.colledk.onboarding.ui.profilesetup.SelectPicturePane
import com.colledk.onboarding.ui.profilesetup.SelectTopicsPane
import kotlinx.coroutines.launch

@Composable
internal fun ProfileSetupPane(
    modifier: Modifier = Modifier,
    pages: List<ProfileSetupDestination> = entries
) {

    Box(
        modifier = modifier.then(
            Modifier.background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.surfaceTint,
                        MaterialTheme.colorScheme.secondaryContainer
                    )
                )
            )
        )
    ) {
        val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
        val pagesPerScreen =
            if (windowSizeClass.windowWidthSizeClass != WindowWidthSizeClass.COMPACT) 2 else 1

        val scope = rememberCoroutineScope()

        val pagerState = rememberPagerState { pages.size }
        HorizontalPager(
            modifier = Modifier.fillMaxSize(),
            state = pagerState,
            pageSize = object : PageSize {
                override fun Density.calculateMainAxisPageSize(
                    availableSpace: Int,
                    pageSpacing: Int
                ): Int {
                    return ((availableSpace - (pagesPerScreen - 1) * pageSpacing) / pagesPerScreen)
                }
            },
            contentPadding = PaddingValues(bottom = 92.dp, top = 48.dp)
        ) { page ->
            ProfileSetupPage(page = pages[page], modifier = Modifier.fillMaxSize())
        }

        if ((pagerState.currentPage == pagerState.pageCount - 1 || (pagesPerScreen > 1 && pagerState.currentPage + 1 == pagerState.pageCount - 1)).not()) {
            Button(
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(pagerState.pageCount)
                    }
                },
                modifier = Modifier.align(Alignment.TopEnd),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                Text(
                    text = stringResource(id = R.string.setup_profile_skip),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        Column(
            modifier = Modifier
                .padding(bottom = 12.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            NextButton(
                modifier = Modifier
                    .fillMaxWidth(.5f)
                    .heightIn(min = 48.dp)
            ) {
                scope.launch {
                    pagerState.animateScrollToPage(pagerState.currentPage + 1)
                }
            }
            Row {
                repeat(pagerState.pageCount) { index ->
                    val color =
                        if (pagerState.currentPage == index || (pagesPerScreen > 1 && pagerState.currentPage + 1 == index)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(8.dp)
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun NextButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        elevation = ButtonDefaults.buttonElevation(2.dp)
    ) {
        Text(
            text = stringResource(id = R.string.setup_profile_next),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
private fun ProfileSetupPage(
    page: ProfileSetupDestination,
    modifier: Modifier = Modifier
) {
    when (page) {
        ADD_PICTURE -> SelectPicturePane(modifier = modifier)
        ADD_DESCRIPTION -> AddDescriptionPane(modifier = modifier)
        SELECT_LANGUAGES -> SelectLanguagesPane(modifier = modifier)
        SELECT_TOPICS -> SelectTopicsPane(modifier = modifier)
        SELECT_GENDER -> SelectGender(modifier = modifier)
    }
}