package com.colledk.home.data.remote

import com.colledk.home.data.remote.model.PostRemote
import com.colledk.home.data.remote.model.ReplyRemote
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import okio.IOException
import timber.log.Timber

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

            val replies = db
                .collection(POST_PATH)
                .document(postId)
                .collection(REPLY_PATH)
                .orderBy("timestamp")
                .get()
                .await()
                .toObjects(ReplyRemote::class.java)

            return if (post == null) {
                Result.failure(IOException())
            } else {
                Result.success(value = post.copy(replies = replies))
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
            db.collection(POST_PATH).document(postId).set(post.copy(id = postId)).await()
//            post.replies.forEach {
//                db.collection(POST_PATH).document(postId).collection(REPLY_PATH).add(it).await()
//            }
            return getPost(postId = postId)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun updatePost(post: PostRemote): Result<Unit> {
        try {
            db.collection(POST_PATH).document(post.id).set(post).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun likePost(postId: String, userId: String): Result<Unit> {
        try {
            db.collection(POST_PATH).document(postId).update(LIKE_PATH, FieldValue.arrayUnion(userId)).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun removePostLike(postId: String, userId: String): Result<Unit> {
        try {
            db.collection(POST_PATH).document(postId).update(LIKE_PATH, FieldValue.arrayRemove(userId)).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    suspend fun addReply(postId: String, reply: ReplyRemote): Result<Unit> {
        try {
            db.collection(POST_PATH).document(postId).collection(REPLY_PATH).add(reply).await()
            return Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    companion object {
        const val POST_PATH = "posts"
        const val LIKE_PATH = "likes"
        const val REPLY_PATH = "replies"
    }
}