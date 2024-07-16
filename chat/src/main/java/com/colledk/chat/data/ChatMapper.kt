package com.colledk.chat.data

import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.colledk.chat.data.remote.model.AiChatRemote
import com.colledk.chat.data.remote.model.AiMessageRemote
import com.colledk.chat.data.remote.model.ChatRemote
import com.colledk.chat.data.remote.model.MessageRemote
import com.colledk.chat.domain.model.AiChat
import com.colledk.chat.domain.model.AiItem
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.user.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun AiChatRemote.mapToDomain(): AiChat {
    return AiChat(
        id = id,
        messages = messages.map { it.mapToChatMessage() },
        ai = AiItem.valueOf(aiName.uppercase())
    )
}

fun AiMessageRemote.mapToChatMessage(): ChatMessage {
    return ChatMessage(
        role = ChatRole(role),
        content = content
    )
}

fun AiChat.mapToRemote(userId: String): AiChatRemote {
    return AiChatRemote(
        id = id,
        messages = messages.map { it.mapToAiMessage() },
        userId = userId,
        aiName = ai.name
    )
}

fun ChatMessage.mapToAiMessage(): AiMessageRemote {
    return AiMessageRemote(
        role = role.role,
        content = content.orEmpty()
    )
}

fun Message.mapToRemote(): MessageRemote {
    return MessageRemote(
        id = id,
        senderId = sender.id,
        content = content,
        timestamp = timestamp
    )
}

fun ChatRemote.mapToDomain(users: List<User>): Chat {
    return Chat(
        id = id,
        users = users,
        messages = messages.mapNotNull { message ->
            users.find { it.id ==  message.senderId }?.let {
                message.mapToDomain(it)
            }
        }
    )
}

private fun MessageRemote.mapToDomain(user: User): Message {
    return Message(
        id = id,
        sender = user,
        content = content,
        time = timestamp.toTimeString(),
        timestamp = timestamp,
        date = timestamp.toDateString()
    )
}

/**
 * Convert a timestamp into a string text.
 *
 * If the timestamp is within the same day, it will print Today.
 *
 * If the timestamp is from yesterday, it will print Yesterday.
 *
 * If the timestamp is within the same week, it will print the day, i.e. Monday.
 *
 * Else the timestamp will print the long date, i.e. November 3, 2016.
 */
private fun Long.toDateString(): String {
    val date = DateTime(this)
    return when {
        date.isToday() ->  {
            "Today"
        }
        date.isYesterday() -> {
            "Yesterday"
        }
        date.isCurrentWeek() -> {
            date.dayOfWeek().asText
        }
        else -> {
            date.toString(DateTimeFormat.longDate())
        }
    }
}


/**
 * Convert a timestamp into a string text.
 *
 * If the timestamp is within the same day, it will print HH:mm, i.e. 12:53.
 *
 * If the timestamp is within the same week, it will print the day, i.e. Monday.
 *
 * Else the timestamp will print the shorted date, i.e. 12/12/13.
 */
private fun Long.toTimeString(): String {
    val date = DateTime(this)
    return when {
        date.isToday() -> {
            date.toString("HH:mm")
        }
        date.isCurrentWeek() -> {
            date.dayOfWeek().asText
        }
        else -> {
            date.toString(DateTimeFormat.shortDate())
        }
    }
}

private fun DateTime.isToday(): Boolean {
    val today = DateTime.now()
    val tomorrow = today.plusDays(1)

    val startOfToday = today.withTimeAtStartOfDay()
    val startOfTomorrow = tomorrow.withTimeAtStartOfDay()
    return startOfToday <= this && this < startOfTomorrow
}

private fun DateTime.isYesterday(): Boolean {
    val today = DateTime.now()
    val yesterday = today.minusDays(1)

    val startOfToday = today.withTimeAtStartOfDay()
    val startOfYesterday = yesterday.withTimeAtStartOfDay()
    return startOfYesterday <= this && this < startOfToday
}

private fun DateTime.isCurrentWeek(): Boolean {
    val today = DateTime.now()

    return today.weekOfWeekyear() == this.weekOfWeekyear()
}