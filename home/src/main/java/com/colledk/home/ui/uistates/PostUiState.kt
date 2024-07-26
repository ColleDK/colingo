package com.colledk.home.ui.uistates

import com.colledk.home.domain.model.Post

sealed class PostUiState {
    data object Loading: PostUiState()
    data class Data(val post: Post): PostUiState()
}