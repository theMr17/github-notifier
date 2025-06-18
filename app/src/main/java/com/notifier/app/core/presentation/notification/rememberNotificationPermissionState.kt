package com.notifier.app.core.presentation.notification

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale

/**
 * Remembers and provides the current state of the notification permission using
 * [Manifest.permission.POST_NOTIFICATIONS] (available from Android 13/TIRAMISU).
 *
 * This function wraps the Accompanist [rememberPermissionState] for notifications into
 * a [NotificationPermissionState] interface for simplified use in UI components.
 *
 * @return a [NotificationPermissionState] instance containing the current permission status,
 * rationale flag, and a method to launch the permission request.
 */
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun rememberNotificationPermissionState(): NotificationPermissionState {
    val state = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)

    return object : NotificationPermissionState {
        override val shouldShowRationale: Boolean
            get() = state.status.shouldShowRationale

        override val isGranted: Boolean
            get() = state.status.isGranted

        override fun requestPermission() {
            state.launchPermissionRequest()
        }
    }
}
