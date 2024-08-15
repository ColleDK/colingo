package com.colledk.user.domain.usecase

import android.net.Uri
import com.colledk.user.domain.repository.UserRepository

class DeleteProfilePictureUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(picture: Uri): Result<Unit> {
        return repository.deleteProfilePicture(picture = picture)
    }
}