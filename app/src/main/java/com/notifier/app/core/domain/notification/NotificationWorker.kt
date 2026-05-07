package com.notifier.app.core.domain.notification

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.notifier.app.core.data.persistence.DataStoreManager
import com.notifier.app.core.domain.util.onError
import com.notifier.app.core.domain.util.onSuccess
import com.notifier.app.notification.domain.NotificationDataSource
import com.notifier.app.notification.presentation.model.toNotificationUi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/**
 * A periodic [CoroutineWorker] that fetches GitHub notifications and displays
 * Android system notifications for any new ones.
 *
 * It uses [DataStoreManager] to persist the IDs of notifications that have already
 * been shown, so duplicate system notifications are never displayed.
 *
 * @property notificationDataSource Used to fetch notifications from the GitHub API.
 * @property notificationHandler Used to display Android system notifications.
 * @property dataStoreManager Used to persist which notification IDs have been shown.
 */
@HiltWorker
class NotificationWorker @AssistedInject constructor(
    private val notificationDataSource: NotificationDataSource,
    private val notificationHandler: NotificationHandler,
    private val dataStoreManager: DataStoreManager,
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    companion object {
        private const val TAG = "NotificationWorker"

        /** Maximum number of shown IDs to retain to prevent unbounded growth. */
        private const val MAX_SHOWN_IDS = 500
    }

    override suspend fun doWork(): Result {
        try {
            // Retrieve previously shown notification IDs
            var shownIds: Set<String> = emptySet()
            dataStoreManager.getShownNotificationIds()
                .onSuccess { shownIds = it }
                .onError { error ->
                    Log.w(TAG, "Failed to read shown notification IDs: $error")
                }

            notificationDataSource.getNotifications(includeRead = false)
                .onSuccess { notifications ->
                    val newNotifications = notifications.filter { it.id !in shownIds }

                    if (newNotifications.isEmpty()) {
                        Log.d(TAG, "No new notifications to show")
                        return Result.success()
                    }

                    val newIds = mutableSetOf<String>()
                    for (notification in newNotifications) {
                        val ui = notification.toNotificationUi()
                        notificationHandler.showNotification(
                            id = notification.id.hashCode(),
                            title = ui.title,
                            message = ui.description,
                            channel = AppNotificationChannel.GITHUB,
                            url = ui.redirectUrl,
                        )
                        newIds.add(notification.id)
                    }

                    // Merge with existing shown IDs, capping at MAX_SHOWN_IDS
                    val updatedIds = (shownIds + newIds).let { all ->
                        if (all.size > MAX_SHOWN_IDS) {
                            all.drop(all.size - MAX_SHOWN_IDS).toSet()
                        } else {
                            all
                        }
                    }
                    dataStoreManager.setShownNotificationIds(updatedIds)

                    Log.d(TAG, "Showed ${newIds.size} new notifications (total tracked: ${updatedIds.size})")
                    return Result.success()
                }
                .onError { error ->
                    Log.e(TAG, "Failed to fetch notifications: $error")
                    return Result.retry()
                }
        } catch (e: Exception) {
            Log.e(TAG, "Unexpected exception in doWork", e)
            return Result.retry()
        }
        return Result.failure()
    }
}
