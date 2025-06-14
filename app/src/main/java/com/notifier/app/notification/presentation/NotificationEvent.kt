package com.notifier.app.notification.presentation

import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.PersistenceError

sealed interface NotificationEvent {
    data class NetworkErrorEvent(val error: NetworkError) : NotificationEvent
    data class PersistenceErrorEvent(val error: PersistenceError) : NotificationEvent
}
