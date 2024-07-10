package com.colledk.home.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.pagination.HomePagingSource
import com.colledk.home.domain.usecase.CreatePostUseCase
import com.colledk.user.domain.model.Topic
import com.colledk.user.domain.usecase.GetCurrentUserUseCase
import com.colledk.user.domain.usecase.GetUserUseCase
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val db: FirebaseFirestore,
    private val getUserUseCase: GetUserUseCase,
    private val createPostUseCase: CreatePostUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {
    val posts = Pager(
        PagingConfig(pageSize = HomePagingSource.PAGE_SIZE)
    ) {
        // TODO figure out a better setup for this, maybe move logic to repository
        HomePagingSource(
            db = db,
            getUserUseCase = getUserUseCase
        )
    }.flow.cachedIn(viewModelScope)

    fun createPost(text: String, topics: List<Topic>) {
        viewModelScope.launch {
            getCurrentUserUseCase().onSuccess {
                createPostUseCase(
                    Post(
                        user = it,
                        content = text,
                        topics = topics,
                        timestamp = DateTime.now(),
                        likes = 0,
                        replies = emptyList(),
                        id = ""
                    )
                )
            }
        }
    }
}