package com.notifier.app.notification.domain

import java.time.ZonedDateTime

data class Notification(
    val id: String,
    val lastReadAt: ZonedDateTime,
    val reason: String,
    val repository: Repository,
    val subject: Subject,
    val subscriptionUrl: String,
    val unread: Boolean,
    val updatedAt: ZonedDateTime,
    val url: String,
)
