package com.colledk.user.domain.pagination

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.colledk.user.data.mapToDomain
import com.colledk.user.data.remote.UserRemoteDataSource
import com.colledk.user.data.remote.model.UserRemote
import com.colledk.user.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) : PagingSource<QuerySnapshot, User>() {

    private val query = db.collection(UserRemoteDataSource.USERS_PATH)
        .whereNotEqualTo("id", auth.currentUser?.uid)
        .limit(PAGE_SIZE.toLong())

    override fun getRefreshKey(state: PagingState<QuerySnapshot, User>): QuerySnapshot? {
        return null
    }

    override suspend fun load(params: LoadParams<QuerySnapshot>): LoadResult<QuerySnapshot, User> {
        return try {
            val currentPage = params.key ?: query.get().await()
            val lastVisibleUser = currentPage.documents[currentPage.size() - 1]
            val nextPage = query.startAfter(lastVisibleUser).get().await()

            LoadResult.Page(
                data = currentPage.toObjects(UserRemote::class.java).map { it.mapToDomain() },
                prevKey = null,
                nextKey = nextPage
            )
        } catch (e: Exception) {
            LoadResult.Error(throwable = e)
        }
    }

    companion object {
        const val PAGE_SIZE = 15
    }
}