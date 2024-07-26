package com.colledk.user.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Locale

@Parcelize
data class UserLanguage(
    val language: Locale,
    val proficiency: LanguageProficiency
) : Parcelable

@Parcelize
enum class LanguageProficiency : Parcelable {
    BEGINNER,
    INTERMEDIATE,
    FLUENT
}