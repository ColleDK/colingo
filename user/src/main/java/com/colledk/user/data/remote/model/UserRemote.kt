package com.colledk.user.data.remote.model

data class UserRemote(
    val id: String = "",
    val name: String = "",
    val birthday: Long = 0L,
    val profilePictures: List<String> = emptyList(),
    val description: String = "",
    val location: AddressRemote = AddressRemote(),
    val languages: List<UserLanguageRemote> = emptyList(),
    val gender: GenderRemote = GenderRemote.OTHER,
    val friends: List<String> = emptyList(),
    val chats: List<String> = emptyList(),
    val friendRequests: List<String> = emptyList(),
    val topics: List<TopicRemote> = emptyList(),
    val aiChats: List<String> = emptyList()
)
