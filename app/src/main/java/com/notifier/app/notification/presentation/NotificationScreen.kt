package com.notifier.app.notification.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.notifier.app.notification.presentation.components.NotificationItem
import com.notifier.app.ui.theme.GitHubNotifierTheme

@Composable
fun NotificationScreen(
    state: NotificationState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(state.notifications.size) {
            NotificationItem(state.notifications[it])
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun NotificationScreenPreview() {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            NotificationScreen(
                state = NotificationState(),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
