package com.notifier.app.notification.data.networking.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NotificationDto(
    @SerialName("id")
    val id: String,
    @SerialName("last_read_at")
    val lastReadAt: String,
    @SerialName("reason")
    val reason: String,
    @SerialName("repository")
    val repositoryDto: RepositoryDto,
    @SerialName("subject")
    val subjectDto: SubjectDto,
    @SerialName("subscription_url")
    val subscriptionUrl: String,
    @SerialName("unread")
    val unread: Boolean,
    @SerialName("updated_at")
    val updatedAt: String,
    @SerialName("url")
    val url: String,
)
