package com.notifier.app.core.domain.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.notifier.app.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHandler @Inject constructor(
    @ApplicationContext private val applicationContext: Context,
) {
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

    fun createNotificationChannels() {
        AppNotificationChannel.entries.forEach { channel ->
            val notificationChannel = NotificationChannel(
                channel.id,
                channel.displayName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = channel.description
            }

            val notificationManager = applicationContext.getSystemService(
                Context.NOTIFICATION_SERVICE
            ) as NotificationManager

            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
}
