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
    val isRead: Boolean,
    val redirectUrl: String,
)

fun Notification.toNotificationUi(): NotificationUi {
    val iconResId = when (subject.type) {
        "PullRequest" -> R.drawable.ic_pull
        "Issue" -> R.drawable.ic_issue
        "Discussion" -> R.drawable.ic_discussion
        else -> R.drawable.ic_issue
    }
    val number = subject.url.substringAfterLast("/").toInt()
    val repositoryInfo = "${repository.fullName} #${number}"
    val title = subject.title
    val description = when (reason) {
        "assign" -> "You were assigned to this ${subject.type.lowercase()}."
        "author" -> "You're the author of this ${subject.type.lowercase()}."
        "comment" -> "Someone commented on this ${subject.type.lowercase()}."
        "invitation" -> "You've been invited to collaborate on this repository."
        "manual" -> "You manually subscribed to this ${subject.type.lowercase()}."
        "mention" -> "You were mentioned in this ${subject.type.lowercase()}."
        "review_requested" -> "You were requested to review this pull request."
        "security_alert" -> "There is a security alert related to this repository."
        "state_change" -> "This ${subject.type.lowercase()} was updated."
        "subscribed" -> "You are subscribed to updates on this ${subject.type.lowercase()}."
        "team_mention" -> "Your team was mentioned in this ${subject.type.lowercase()}."
        else -> "There’s an update to this ${subject.type.lowercase()}."
    }
    val time = updatedAt.toRelativeTimeString()
    val isRead = !unread
    val redirectUrl = getHtmlUrlFromApiUrl(subject.url, repository.htmlUrl)

    return NotificationUi(
        id,
        iconResId,
        repositoryInfo,
        title,
        description,
        time,
        isRead,
        redirectUrl
    )
}

fun getHtmlUrlFromApiUrl(apiUrl: String?, repositoryHtmlUrl: String): String {
    if (apiUrl.isNullOrBlank()) return repositoryHtmlUrl

    val regex = Regex("""https://api.github.com/repos/([^/]+)/([^/]+)/([^/]+)/(.+)""")
    val match = regex.find(apiUrl) ?: return repositoryHtmlUrl
    val (owner, repo, type, rest) = match.destructured

    val path = when (type) {
        "pulls" -> "pull/$rest"
        "issues" -> "issues/$rest"
        "commits" -> "commit/$rest"
        "releases" -> "releases/$rest"
        "discussions" -> "discussions/$rest"
        "comments" -> "issues/comments/$rest"
        else -> return repositoryHtmlUrl
    }

    return "https://github.com/$owner/$repo/$path"
}
