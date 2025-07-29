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

/**
 * A composable function that displays the GitHub notification screen UI.
 *
 * Based on the current [NotificationState], this screen renders either a loading indicator or
 * a list of notifications:
 * - **Loading State**: A centered [CircularProgressIndicator] is shown when [NotificationState.isLoading] is `true`.
 * - **Loaded State**: A scrollable list of [NotificationItem]s is displayed when notifications are available.
 *
 * @param state The current state of the notification screen, including loading status and the list of notifications.
 * @param modifier An optional [Modifier] to be applied to the root layout.
 */
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
        items(
            count = state.notifications.size,
            key = { index -> state.notifications[index].id }
        ) { index ->
            NotificationItem(state.notifications[index])
        }
    }
}

/**
 * Preview parameter provider for displaying different notification states in previews.
 *
 * Provides sample values for the [NotificationState] to simulate both loading and loaded states
 * with fake notification data, including randomized icons and read/unread states.
 */
class NotificationStateParameterProvider : PreviewParameterProvider<NotificationState> {
    private val notificationUiList = (1..10).map {
        val iconResIdList = listOf(
            R.drawable.ic_pull,
            R.drawable.ic_issue,
            R.drawable.ic_discussion
        )
        NotificationUi(
            id = it.toString(),
            iconResId = iconResIdList.random(),
            repositoryInfo = "theMr17/github-notifier #1234",
            title = "Title of the notification goes here " +
                    "(e.g., comment, issue, or pull request update)",
            description = "This is a brief description of the notification content.",
            time = "${it}h",
            isRead = Random.nextBoolean(),
            redirectUrl = "https://github.com/theMr17/github-notifier",
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

/**
 * Preview of the [NotificationScreen] composable with dynamic colors and light/dark theme support.
 *
 * This preview helps visualize how the Notification screen looks in different visual states.
 *
 * @param state The preview parameter simulating different [NotificationState]s.
 */
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
