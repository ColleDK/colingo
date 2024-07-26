package com.colledk.user.data.remote.model

data class UserLanguageRemote(
    val code: String = "",
    val proficiency: LanguageProficiencyRemote = LanguageProficiencyRemote.BEGINNER
)

enum class LanguageProficiencyRemote {
    BEGINNER,
    INTERMEDIATE,
    FLUENT
}