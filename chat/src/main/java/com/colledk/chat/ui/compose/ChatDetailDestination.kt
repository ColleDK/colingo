package com.colledk.chat.ui.compose

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
sealed class ChatDetailDestination: Parcelable {
    data class AiChatDestination(val id: String): ChatDetailDestination()
    data class MemberChatDestination(val id: String): ChatDetailDestination()
}