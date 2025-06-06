package com.notifier.app.notification.presentation

import androidx.compose.runtime.Immutable
import com.notifier.app.notification.domain.model.Notification

@Immutable
data class NotificationState(
    val isLoading: Boolean = false,
    val notifications: List<Notification> = emptyList(),
)
