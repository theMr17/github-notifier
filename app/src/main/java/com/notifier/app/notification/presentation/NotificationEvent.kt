package com.notifier.app.notification.presentation

import com.notifier.app.core.domain.util.NetworkError

sealed interface NotificationEvent {
    data class NetworkErrorEvent(val error: NetworkError) : NotificationEvent
}
