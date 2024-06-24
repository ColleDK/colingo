package com.colledk.onboarding.ui.profilesetup.uistates

import androidx.compose.runtime.Stable
import com.colledk.onboarding.domain.Topic

@Stable
data class TopicsUiState(
    val selectedTopics: List<Topic> = emptyList()
)
