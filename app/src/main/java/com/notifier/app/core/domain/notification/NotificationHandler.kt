package com.notifier.app.core.domain.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
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
     * @param url The URL to open in the browser when the notification is clicked.
     */
    fun showNotification(
        id: Int,
        title: String,
        message: String,
        channel: AppNotificationChannel,
        url: String,
    ) {
        val pendingIntent = PendingIntent.getActivity(
            /* context = */ applicationContext,
            /* requestCode = */ id,
            /* intent = */ Intent(Intent.ACTION_VIEW, url.toUri()),
            /* flags = */ PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(applicationContext, channel.id)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
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
