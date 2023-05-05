package com.colledk.onboarding.ui.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.colledk.onboarding.ui.OnboardingViewModel
import com.colledk.onboarding.ui.R
import com.colledk.onboarding.ui.navigation.navigateToSignUp

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun OnboardingScreen(
    navController: NavHostController,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val tabs by viewModel.tabs.collectAsState()
    val pagerState = rememberPagerState()

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            tabs.getOrNull(pagerState.currentPage)?.name?.let {
                Text(
                    text = stringResource(id = it),
                    modifier = Modifier.fillMaxWidth(),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    ) { padding ->
        HorizontalPager(
            pageCount = tabs.size,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = padding)
        ) { page ->
            when (page) {
                R.string.onboarding_create_user_page -> {
                    navController.navigateToSignUp()
                }
            }
        }
    }
}