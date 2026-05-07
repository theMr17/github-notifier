package com.notifier.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.notifier.app.core.domain.notification.NotificationHandler
import com.notifier.app.core.domain.notification.NotificationWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class GithubNotifierApp : Application(), Configuration.Provider {
    @Inject
    lateinit var notificationHandler: NotificationHandler

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    companion object {
        private const val NOTIFICATION_WORK_NAME = "github_notification_sync"
    }

    override fun onCreate() {
        super.onCreate()

        notificationHandler.createNotificationChannels()

        val notificationWork = PeriodicWorkRequestBuilder<NotificationWorker>(
            repeatInterval = 15,
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        ).build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            NOTIFICATION_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            notificationWork
        )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
