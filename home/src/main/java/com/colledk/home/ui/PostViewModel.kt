package com.colledk.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.model.Reply
import com.colledk.home.domain.usecase.AddReplyUseCase
import com.colledk.home.domain.usecase.FormatNumberUseCase
import com.colledk.home.domain.usecase.GetPostUseCase
import com.colledk.home.domain.usecase.LikePostUseCase
import com.colledk.home.domain.usecase.RemovePostLikeUseCase
import com.colledk.home.ui.uistates.PostUiState
import com.colledk.user.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class PostViewModel @Inject constructor(
    private val getPostUseCase: GetPostUseCase,
    private val addReplyUseCase: AddReplyUseCase,
    private val formatNumberUseCase: FormatNumberUseCase,
    private val likePostUseCase: LikePostUseCase,
    private val removePostLikeUseCase: RemovePostLikeUseCase
) : ViewModel() {
    private val _uiState: MutableStateFlow<PostUiState> = MutableStateFlow(PostUiState.Loading)
    val uiState: StateFlow<PostUiState> = _uiState

    private val _error: Channel<Throwable> = Channel(Channel.BUFFERED)
    val error: Flow<Throwable> = _error.receiveAsFlow()

    fun retrievePost(id: String) {
        if (id != (_uiState.value as? PostUiState.Data)?.post?.id) {
            _uiState.value = PostUiState.Loading
        }
        viewModelScope.launch {
            getPostUseCase(id).onSuccess {
                updateUiState(it)
            }.onFailure {
                _error.trySend(it)
            }
        }
    }

    fun addReply(
        postId: String,
        reply: String,
        currentUser: User
    ) {
        viewModelScope.launch {
            addReplyUseCase(
                postId = postId,
                reply = Reply(
                    user = currentUser,
                    content = reply,
                    timestamp = DateTime.now(),
                    replies = emptyList(),
                    id = ""
                )
            ).onFailure {
                _error.trySend(it)
            }.onSuccess {
                retrievePost(id = postId)
            }
        }
    }

    fun likePost(post: Post, userId: String) {
        viewModelScope.launch {
            likePostUseCase(postId = post.id, userId = userId).onFailure {
                _error.trySend(it)
            }.onSuccess {
                retrievePost(post.id)
            }
        }
    }

    fun removeLike(post: Post, userId: String) {
        viewModelScope.launch {
            removePostLikeUseCase(postId = post.id, userId = userId).onFailure {
                _error.trySend(it)
            }.onSuccess {
                retrievePost(post.id)
            }
        }
    }

    fun formatNumber(num: Number): String = formatNumberUseCase(num = num)

    private fun updateUiState(
        post: Post
    ) {
        _uiState.value = PostUiState.Data(post = post)
    }
}