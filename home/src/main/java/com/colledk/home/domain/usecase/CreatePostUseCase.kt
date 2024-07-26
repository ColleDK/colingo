package com.colledk.home.domain.usecase

import com.colledk.home.domain.model.Post
import com.colledk.home.domain.model.Reply
import com.colledk.home.domain.repository.HomeRepository
import com.colledk.user.domain.model.User
import org.joda.time.DateTime
import javax.inject.Inject

class CreatePostUseCase @Inject constructor(
    private val repository: HomeRepository
){
    suspend operator fun invoke(post: Post): Result<Post> {
        return repository.createPost(post = post)
    }
}