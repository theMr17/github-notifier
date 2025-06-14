package com.notifier.app.notification.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.notifier.app.R
import com.notifier.app.notification.presentation.components.NotificationItem
import com.notifier.app.notification.presentation.model.NotificationUi
import com.notifier.app.ui.theme.GitHubNotifierTheme
import kotlin.random.Random

@Composable
fun NotificationScreen(
    state: NotificationState,
    modifier: Modifier = Modifier,
) {
    if (state.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.notifications.size) {
            NotificationItem(state.notifications[it])
        }
    }
}

class NotificationStateParameterProvider : PreviewParameterProvider<NotificationState> {
    private val notificationUiList = (1..10).map {
        val iconResIdList = listOf(
            R.drawable.ic_pull,
            R.drawable.ic_issue
        )
        val randomIconResIdIndex = Random.nextInt(until = iconResIdList.size)
        NotificationUi(
            id = it.toString(),
            iconResId = iconResIdList[randomIconResIdIndex],
            repositoryInfo = "theMr17/github-notifier #1234",
            title = "Title of the notification goes here " +
                    "(e.g., comment, issue, or pull request update)",
            description = "This is a brief description of the notification content. It can be a " +
                    "comment, issue, or pull request update.",
            time = "${it}h",
            isRead = Random.nextBoolean(),
        )
    }

    override val values: Sequence<NotificationState>
        get() = sequenceOf(
            NotificationState(
                isLoading = true,
                notifications = emptyList()
            ),
            NotificationState(
                isLoading = false,
                notifications = notificationUiList
            )
        )
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun NotificationScreenPreview(
    @PreviewParameter(NotificationStateParameterProvider::class)
    state: NotificationState,
) {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            NotificationScreen(
                state = state,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
