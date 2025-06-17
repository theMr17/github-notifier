package com.notifier.app.core.domain.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.notifier.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Handles the creation and display of in-app notifications.
 *
 * This class is responsible for:
 * - Creating required [NotificationChannel]s.
 * - Showing notifications using [NotificationCompat.Builder].
 *
 * @property applicationContext The application-level context used to access system services.
 */
@Singleton
class NotificationHandler @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {

    /**
     * Displays a notification with the given details.
     *
     * @param id The unique ID for this notification (used to update or cancel it).
     * @param title The title displayed in the notification.
     * @param message The body content of the notification.
     * @param channel The [AppNotificationChannel] that defines the channel for this notification.
     */
    fun showNotification(
        id: Int,
        title: String,
        message: String,
        channel: AppNotificationChannel,
    ) {
        val notification = NotificationCompat.Builder(applicationContext, channel.id)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .build()

        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        notificationManager.notify(id, notification)
    }

    /**
     * Creates all required notification channels used by the app.
     *
     * This must be called during app startup on Android O+ (API 26+) to register
     * channels with the system before sending notifications.
     */
    fun createNotificationChannels() {
        val notificationManager = applicationContext.getSystemService(
            Context.NOTIFICATION_SERVICE
        ) as NotificationManager

        AppNotificationChannel.entries.forEach { channel ->
            val notificationChannel = NotificationChannel(
                channel.id,
                channel.displayName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = channel.description
            }

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
