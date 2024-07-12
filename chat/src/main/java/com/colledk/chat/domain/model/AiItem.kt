package com.colledk.chat.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.colledk.chat.R

enum class AiItem(
    @DrawableRes val image: Int,
    @StringRes val description: Int,
    val systemMsg: String
) {
    LOUISE(
        image = R.drawable.louise,
        description = R.string.chatbot_louise_description,
        systemMsg = "You are a spanish teacher, named Louise, trying to teach spanish to a student. You may only answer in spanish." +
                "You will correct any mistakes by the student, but give examples about how to correct it for the next time!"
    )
}
