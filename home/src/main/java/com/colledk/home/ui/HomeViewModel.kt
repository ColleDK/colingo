package com.colledk.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.pagination.HomePagingSource
import com.colledk.home.domain.usecase.CreatePostUseCase
import com.colledk.home.domain.usecase.LikePostUseCase
import com.colledk.home.domain.usecase.RemovePostLikeUseCase
import com.colledk.home.domain.usecase.UpdatePostUseCase
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.User
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.GetUserUseCase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val getUserUseCase: GetUserUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val removePostLikeUseCase: RemovePostLikeUseCase
) : ViewModel() {
    val posts = Pager(
        PagingConfig(pageSize = HomePagingSource.PAGE_SIZE)
    ) {
        // TODO figure out a better setup for this, maybe move logic to repository
        HomePagingSource(
            db = db,
            getUserUseCase = getUserUseCase
        ).also {
            it.updateSorting(direction = _sorting.value)
        }
    }.flow.cachedIn(viewModelScope)
    private val _sorting: MutableStateFlow<Direction> = MutableStateFlow(Direction.DESCENDING)
    val sorting: StateFlow<Direction> = _sorting

    private val _currentUser: MutableStateFlow<User?> = MutableStateFlow(null)
    val currentUser: StateFlow<User?> = _currentUser

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess {
                _currentUser.value = it
            }
        }
    }

    fun createPost(text: String, topics: List<Topic>) {
        viewModelScope.launch {
            _currentUser.value?.let {
                createPostUseCase(
                    Post(
                        user = it,
                        content = text,
                        topics = topics,
                        timestamp = DateTime.now(),
                        likes = emptyList(),
                        replies = emptyList(),
                        id = ""
                    )
                )
            }
        }
    }

    fun likePost(post: Post, userId: String) {
        viewModelScope.launch {
            likePostUseCase(postId = post.id, userId = userId)
        }
    }

    fun removeLike(post: Post, userId: String) {
        viewModelScope.launch {
            removePostLikeUseCase(postId = post.id, userId = userId)
        }
    }

    fun updateSorting(direction: Direction) {
        _sorting.value = direction
    }
}