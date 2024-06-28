package com.colledk.chat.data

import com.colledk.chat.data.remote.model.ChatRemote
import com.colledk.chat.data.remote.model.MessageRemote
import com.colledk.chat.domain.model.Chat
import com.colledk.chat.domain.model.Message
import com.colledk.user.domain.model.User
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

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
        time = timestamp.toDateString(),
        timestamp = timestamp
    )
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
private fun Long.toDateString(): String {
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

private fun DateTime.isCurrentWeek(): Boolean {
    val today = DateTime.now()

    return today.weekOfWeekyear() == this.weekOfWeekyear()
}