package com.notifier.app.notification.presentation

import androidx.compose.runtime.Immutable
import com.notifier.app.notification.presentation.model.NotificationUi

@Immutable
data class NotificationState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationUi> = emptyList(),
)
