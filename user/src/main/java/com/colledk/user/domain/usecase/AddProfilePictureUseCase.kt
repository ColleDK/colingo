package com.colledk.user.domain.usecase

import android.net.Uri
import com.colledk.user.domain.model.User
import com.colledk.user.domain.repository.UserRepository

class AddProfilePictureUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(uri: Uri): Result<User> {
        return repository.addProfilePicture(picture = uri)
    }
}