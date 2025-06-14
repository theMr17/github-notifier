package com.notifier.app.core.domain.notification

enum class AppNotificationChannel(
    val id: String,
    val displayName: String,
    val description: String,
) {
    GITHUB(
        id = "github_channel",
        displayName = "GitHub Notifications",
        description = "Channel for GitHub related notifications"
    ),
}
