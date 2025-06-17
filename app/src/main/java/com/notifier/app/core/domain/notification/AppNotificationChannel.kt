package com.notifier.app.core.domain.notification

/**
 * Represents the various notification channels used within the app.
 *
 * Each enum entry defines the [id], [displayName], and [description]
 * associated with a specific type of notification channel.
 *
 * These channels are used to categorize and manage notifications for the entire app.
 */
enum class AppNotificationChannel(
    /**
     * The unique ID for the notification channel.
     * Used when creating and referencing the channel in the notification system.
     */
    val id: String,

    /** The user-visible name of the notification channel. */
    val displayName: String,

    /** The user-visible description of what types of notifications this channel delivers. */
    val description: String,
) {
    /**
     * Channel for delivering notifications related to GitHub activity,
     * such as pull requests, issues, and mentions.
     */
    GITHUB(
        id = "github_channel",
        displayName = "GitHub Notifications",
        description = "Channel for GitHub related notifications"
    ),
}
