package com.notifier.app.notification.presentation

import com.notifier.app.core.domain.util.NetworkError

/**
 * A sealed interface representing different notification-related events.
 *
 * These events are used to trigger UI updates or error messages in response
 * to state changes in the Notification screen.
 */
sealed interface NotificationEvent {
    /**
     * Event that triggers a toast or error message for a network-related error.
     *
     * This event is fired when a network error occurs while loading or interacting
     * with notifications.
     *
     * @param error The network error that occurred.
     */
    data class NetworkErrorEvent(val error: NetworkError) : NotificationEvent
}
