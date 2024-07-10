package com.colledk.home.data

import com.colledk.home.data.remote.model.PostRemote
import com.colledk.home.data.remote.model.ReplyRemote
import com.colledk.home.domain.model.Post
import com.colledk.home.domain.model.Reply
import com.colledk.user.data.mapToDomain
import com.colledk.user.data.mapToRemote
import com.colledk.user.domain.model.User
import org.joda.time.DateTime

fun Post.mapToRemote(): PostRemote {
    return PostRemote(
        id = id,
        userId = user.id,
        content = content,
        replies = replies.map { it.mapToRemote() },
        likes = likes,
        timestamp = timestamp.millis,
        topics = topics.map { it.mapToRemote() }
    )
}

internal fun Reply.mapToRemote(): ReplyRemote {
    return ReplyRemote(
        id = id,
        userId = user.id,
        content = content,
        timestamp = timestamp.millis,
        replies = replies.map { it.mapToRemote() }
    )
}

fun PostRemote.mapToDomain(users: List<User>): Post {
    return Post(
        id = id,
        user = users.first { it.id == userId },
        content = content,
        replies = replies.map { it.mapToDomain(users = users) },
        likes = likes,
        timestamp = DateTime(timestamp),
        topics = topics.map { it.mapToDomain() }
    )
}

internal fun ReplyRemote.mapToDomain(users: List<User>): Reply {
    return Reply(
        id = id,
        user = users.first { it.id == userId },
        content = content,
        timestamp = DateTime(timestamp),
        replies = replies.map { it.mapToDomain(users = users) }
    )
}