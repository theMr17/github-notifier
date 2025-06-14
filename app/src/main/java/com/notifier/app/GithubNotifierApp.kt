package com.notifier.app

import android.app.Application
import com.notifier.app.core.domain.notification.NotificationHandler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class GithubNotifierApp : Application() {
    @Inject
    lateinit var notificationHandler: NotificationHandler

    override fun onCreate() {
        super.onCreate()

        notificationHandler.createNotificationChannels()
    }
}
