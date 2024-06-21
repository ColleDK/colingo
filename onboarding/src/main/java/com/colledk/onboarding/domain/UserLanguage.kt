package com.colledk.onboarding.domain

// TODO move to correct module
data class UserLanguage(
    val language: Language,
    val proficiency: LanguageProficiency
)

enum class LanguageProficiency {
    BEGINNER,
    INTERMEDIATE,
    FLUENT
}
