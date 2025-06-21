package com.notifier.app.notification.domain.model

data class Repository(
    val fork: Boolean,
    val forksUrl: String,
    val fullName: String,
    val htmlUrl: String,
    val id: Int,
    val name: String,
    val nodeId: String,
    val notificationsUrl: String,
    val owner: Owner,
    val `private`: Boolean,
)
