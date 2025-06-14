package com.notifier.app.notification.presentation

import com.notifier.app.notification.domain.model.Notification

sealed interface NotificationAction {
    data class OnNotificationItemClick(val notification: Notification) : NotificationAction
}
