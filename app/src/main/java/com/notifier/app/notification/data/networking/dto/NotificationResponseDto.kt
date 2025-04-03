package com.notifier.app.notification.data.networking.dto

import kotlinx.serialization.Serializable

@Serializable
data class NotificationResponseDto(
    val data: List<NotificationDto>,
)
