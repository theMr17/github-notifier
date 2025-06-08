package com.notifier.app.notification.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notifier.app.core.presentation.util.ObserveAsEvents
import kotlinx.serialization.Serializable

@Serializable
data object NotificationScreen

@Composable
fun NotificationRoute(
    viewModel: NotificationViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(events = viewModel.events) { _ ->

    }

    NotificationScreen(
        state = state,
    )
}

