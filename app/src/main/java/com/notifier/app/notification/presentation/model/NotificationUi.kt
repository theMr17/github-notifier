package com.notifier.app.notification.presentation.model

import androidx.annotation.DrawableRes
import com.notifier.app.R
import com.notifier.app.core.presentation.util.toRelativeTimeString
import com.notifier.app.notification.domain.model.Notification

data class NotificationUi(
    val id: String,
    @DrawableRes val iconResId: Int,
    val repositoryInfo: String,
    val title: String,
    val description: String,
    val time: String,
)

fun Notification.toNotificationUi(): NotificationUi {
    val id = id
    val iconResId = when (subject.type) {
        "PullRequest" -> R.drawable.ic_pull
        "Issue" -> R.drawable.ic_issue
        else -> R.drawable.ic_issue
    }
    val repositoryInfo = repository.fullName
    val title = subject.title
    val description = "This is a brief description of the notification content. It can be a " +
            "comment, issue, or pull request update."
    val time = updatedAt.toRelativeTimeString()

    return NotificationUi(
        id,
        iconResId,
        repositoryInfo,
        title,
        description,
        time
    )
}

