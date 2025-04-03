package com.notifier.app.notification.domain

import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.Result

/**
 * Interface defining the data source for fetching notifications.
 *
 * This interface abstracts the implementation of how notifications are retrieved,
 * allowing flexibility for different data sources (e.g., remote API, local database).
 */
interface NotificationDataSource {

    /**
     * Fetches a list of notifications.
     *
     * This function is a suspend function, meaning it should be called from a coroutine
     * or another suspending function. It returns a [Result] that either contains:
     * - A **successful** list of [Notification] objects.
     * - A **failure** with an appropriate [NetworkError].
     *
     * @return A [Result] containing either a list of notifications or a network error.
     */
    suspend fun getNotifications(): Result<List<Notification>, NetworkError>
}
