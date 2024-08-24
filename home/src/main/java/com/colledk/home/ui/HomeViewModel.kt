package com.colledk.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.colledk.chat.domain.usecase.CreateChatUseCase
import com.colledk.common.MessageHandler
import com.colledk.home.R
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.pagination.HomePagingSource
import com.colledk.home.domain.usecase.CreatePostUseCase
import com.colledk.home.domain.usecase.FormatNumberUseCase
import com.colledk.home.domain.usecase.LikePostUseCase
import com.colledk.home.domain.usecase.RemovePostLikeUseCase
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.model.User
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.GetUserUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import okio.IOException
import org.joda.time.DateTime
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val getUserUseCase: GetUserUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val removePostLikeUseCase: RemovePostLikeUseCase,
    private val formatNumberUseCase: FormatNumberUseCase,
    private val createChatUseCase: CreateChatUseCase,
    private val messageHandler: MessageHandler
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

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    init {
        getUser()
    }

    private fun getUser() {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess {
                _currentUser.value = it
            }.onFailure {
                _error.trySend(it)
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
                ).onFailure {
                    messageHandler.displayError(it)
                }.onSuccess {
                    messageHandler.displayMessage(R.string.create_post_success)
                }
            } ?: run {
                messageHandler.displayError(IOException())
            }
        }
    }

    fun likePost(post: Post, userId: String) {
        viewModelScope.launch {
            likePostUseCase(postId = post.id, userId = userId).onFailure {
                _error.trySend(it)
            }
        }
    }

    fun removeLike(post: Post, userId: String) {
        viewModelScope.launch {
            removePostLikeUseCase(postId = post.id, userId = userId).onFailure {
                _error.trySend(it)
            }
        }
    }

    fun createChat(user: String) {
        viewModelScope.launch {
            createChatUseCase(userIds = listOfNotNull(user, auth.currentUser?.uid)).onSuccess {
                messageHandler.displayMessage(R.string.create_chat_success)
            }.onFailure {
                messageHandler.displayError(it)
            }
        }
    }

    fun updateSorting(direction: Direction) {
        _sorting.value = direction
    }

    fun formatNumber(num: Number): String = formatNumberUseCase(num = num)
}