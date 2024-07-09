package com.colledk.user.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLanguage(
    val language: Language = Language(),
    val proficiency: LanguageProficiency = LanguageProficiency.BEGINNER
) : Parcelable

@Parcelize
enum class LanguageProficiency : Parcelable {
    BEGINNER,
    INTERMEDIATE,
    FLUENT
}