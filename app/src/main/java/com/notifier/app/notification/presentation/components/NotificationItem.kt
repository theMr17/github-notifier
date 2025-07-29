package com.notifier.app.notification.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.notifier.app.R
import com.notifier.app.notification.presentation.model.NotificationUi
import com.notifier.app.ui.theme.GitHubNotifierTheme

/**
 * A composable function that displays an individual GitHub notification item.
 *
 * Each notification displays the following:
 * - **Icon**: Visual indicator representing the type (e.g., pull request, issue).
 * - **Repository Info**: Information about the repository and reference number.
 * - **Title**: Brief summary of the notification content.
 * - **Description**: Additional detail (single-line preview).
 * - **Time**: How long ago the event occurred.
 * - **Unread Badge**: A small badge shown if the notification is unread.
 *
 * @param notificationUi The data model representing a GitHub notification.
 * @param modifier An optional [Modifier] for customizing the layout.
 */
@Composable
fun NotificationItem(
    notificationUi: NotificationUi,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            modifier = Modifier
                .padding(end = 16.dp),
            painter = painterResource(id = notificationUi.iconResId),
            contentDescription = null
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = notificationUi.repositoryInfo,
                fontWeight = FontWeight.Light
            )
            Text(
                text = notificationUi.title,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp,
                fontSize = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = notificationUi.description,
                fontWeight = FontWeight.Normal,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Column(
            modifier = Modifier.padding(start = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = notificationUi.time)

            if (!notificationUi.isRead) {
                Badge(
                    modifier = Modifier.size(8.dp),
                )
            }
        }
    }
}

/**
 * A preview of [NotificationItem] using sample data to visualize UI behavior.
 */
@Preview(showBackground = true)
@Composable
private fun NotificationItemPreview() {
    val notificationUi = NotificationUi(
        id = "1",
        iconResId = R.drawable.ic_pull,
        repositoryInfo = "theMr17/github-notifier #1234",
        title = "New comment on your pull request",
        description = "A GitHub user commented: 'Looks good to me!'",
        time = "3h",
        isRead = false,
        redirectUrl = "https://github.com/theMr17/github-notifier",
    )

    GitHubNotifierTheme {
        NotificationItem(notificationUi)
    }
}
