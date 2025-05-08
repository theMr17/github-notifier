package com.notifier.app.notification.presentation

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable

@Serializable
data object NotificationScreen

@Composable
fun NotificationRoute() {
    NotificationScreen()
}

