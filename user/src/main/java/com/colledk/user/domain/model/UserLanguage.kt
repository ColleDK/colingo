package com.colledk.user.domain.model

data class UserLanguage(
    val language: Language,
    val proficiency: LanguageProficiency
)

enum class LanguageProficiency {
    BEGINNER,
    INTERMEDIATE,
    FLUENT
}