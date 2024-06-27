package com.colledk.user.data.remote.model

import com.colledk.user.domain.model.Language

data class UserLanguageRemote(
    val language: LanguageRemote,
    val proficiency: LanguageProficiencyRemote
)

enum class LanguageProficiencyRemote {
    BEGINNER,
    INTERMEDIATE,
    FLUENT
}