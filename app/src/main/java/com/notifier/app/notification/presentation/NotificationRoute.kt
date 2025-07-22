package com.notifier.app.notification.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.serialization.Serializable

/**
 * Navigation route object for the Notification screen.
 *
 * This object is used for deep linking and navigation to the Notification screen within the app.
 */
@Serializable
data object NotificationScreen

/**
 * The NotificationRoute Composable is the entry point to the Notification screen.
 *
 * It observes the notification state from the [NotificationViewModel] and renders
 * the Notification screen with the latest state.
 *
 * @param viewModel The instance of [NotificationViewModel] used for managing notification logic
 * and state.
 */
@Composable
fun NotificationRoute(
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    NotificationScreen(state)
}
