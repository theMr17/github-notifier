package com.notifier.app.notification.domain.model

data class Owner(
    val avatarUrl: String,
    val eventsUrl: String,
    val id: Int,
    val login: String,
    val nodeId: String,
    val type: String,
    val url: String,
)
