package com.colledk.home.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.colledk.home.data.mapToDomain
import com.colledk.home.data.remote.HomeRemoteDataSource.Companion.POST_PATH
import com.colledk.home.data.remote.HomeRemoteDataSource.Companion.REPLY_PATH
import com.colledk.home.data.remote.model.PostRemote
import com.colledk.home.data.remote.model.ReplyRemote
import com.colledk.home.domain.model.Post
import com.colledk.user.domain.usecase.GetUserUseCase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query.Direction
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Inject

class HomePagingSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val getUserUseCase: GetUserUseCase
) : PagingSource<QuerySnapshot, Post>() {
    private val _query = db.collection(POST_PATH)
        .limit(PAGE_SIZE.toLong())

    private var query = _query

    override fun getRefreshKey(state: PagingState<QuerySnapshot, Post>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, Post> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisiblePost = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisiblePost).get().await()

            val result = currentPage.toObjects(PostRemote::class.java)
            val users = getUserIds(result).mapNotNull { getUserUseCase(it).getOrNull() }


            val replies = result.map {
                db.collection(POST_PATH).document(it.id).collection(REPLY_PATH).orderBy("timestamp").get().await().toObjects(ReplyRemote::class.java)
            }

            LoadResult.Page(
                data = result.mapIndexed { index, postRemote ->
                    postRemote.copy(replies = replies[index]).mapToDomain(users = users)
                },
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }

    private fun getUserIds(posts: List<PostRemote>): List<String> {
        val postUser = posts.map { it.userId }
        val replyUser = posts.map { it.replies.map { getReplyUser(it) }.flatten() }.flatten()
        return (postUser + replyUser).distinct()
    }

    private fun getReplyUser(reply: ReplyRemote): List<String> {
        return listOf(reply.userId) + reply.replies.map { getReplyUser(it) }.flatten()
    }

    fun updateSorting(direction: Direction) {
        query = _query.orderBy("timestamp", direction)
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}