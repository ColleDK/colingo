package com.colledk.home.ui.uistates

import androidx.paging.compose.LazyPagingItems
import com.colledk.home.domain.model.Post
import com.colledk.user.domain.model.User
import com.google.firebase.firestore.Query.Direction

data class HomeUiState(
    val posts: LazyPagingItems<Post>,
    val currentUser: User,
    val currentSort: Direction
)
