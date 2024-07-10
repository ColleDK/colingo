package com.colledk.home.data.remote

import com.colledk.home.data.remote.model.PostRemote
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import okio.IOException

class HomeRemoteDataSource(
    private val db: FirebaseFirestore
) {
    suspend fun getPost(postId: String): Result<PostRemote> {
        try {
            val post = db
                .collection(POST_PATH)
                .document(postId)
                .get()
                .await()
                .toObject(PostRemote::class.java)

            return if (post == null) {
                Result.failure(IOException())
            } else {
                Result.success(value = post)
            }
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun createPost(post: PostRemote): Result<PostRemote> {
        try {
            // Create a chat
            val postId = db.collection(POST_PATH).add(post).await().id
            // Update the id in the data
            db.collection(POST_PATH).document(postId).set(post.copy(id = postId))
            return getPost(postId = postId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    companion object {
        const val POST_PATH = "posts"
    }
}