package com.colledk.user.domain.usecase

import android.net.Uri
import com.colledk.user.domain.repository.UserRepository

class UploadProfilePicUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(uri: Uri, userId: String): Result<Uri> {
        return repository.uploadProfilePicture(uri = uri, userId = userId)
    }
}