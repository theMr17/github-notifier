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
            modifier = Modifier.padding(end = 16.dp),
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
                fontSize = 18.sp
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

@Preview(showBackground = true)
@Composable
private fun NotificationItemPreview() {
    val notificationUi = NotificationUi(
        id = "1",
        iconResId = R.drawable.ic_pull,
        repositoryInfo = "owner/repo #1234",
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
