package com.notifier.app.core.presentation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.notifier.app.R
import com.notifier.app.ui.theme.GitHubNotifierTheme

/**
 * Wraps a composable with a notification permission check on Android 13+.
 *
 * Displays a prompt to the user requesting notification permission if not granted.
 *
 * @param content The actual content of the screen above which the notification permission
 * prompt will be displayed.
 */
@Composable
fun WithNotificationPermission(
    content: @Composable () -> Unit,
) {
    Column {
        NotificationPermissionHandler()
        content()
    }
}

/**
 * Handles runtime notification permission request for Android 13+ devices.
 *
 * Shows the system dialog once and then falls back to a custom UI prompt
 * to guide the user to settings if permission is denied permanently.
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun NotificationPermissionHandler() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) return

    val context = LocalContext.current
    val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    var hasRequested by rememberSaveable { mutableStateOf(false) }

    // Launch the system permission dialog only once, if applicable
    LaunchedEffect(permissionState.status) {
        if (!hasRequested &&
            !permissionState.status.isGranted &&
            !permissionState.status.shouldShowRationale
        ) {
            hasRequested = true
            permissionState.launchPermissionRequest()
        }
    }

    if (!permissionState.status.isGranted) {
        NotificationPermissionPrompt(
            shouldShowRationale = permissionState.status.shouldShowRationale,
            onRequestPermission = {
                hasRequested = true
                permissionState.launchPermissionRequest()
            },
            onOpenSettings = {
                runCatching {
                    Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.fromParts(
                            /* scheme = */ "package",
                            /* ssp = */ context.packageName,
                            /* fragment = */ null
                        )
                    ).apply {
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }.let(context::startActivity)
                }
            }
        )
    }
}

/**
 * A UI prompt to guide the user for notification permission.
 *
 * If rationale should be shown, it allows retrying the permission request.
 * Otherwise, it suggests navigating to system settings to enable permission manually.
 *
 * @param shouldShowRationale Whether to explain the need for permission again.
 * @param onRequestPermission Callback for retrying the permission request.
 * @param onOpenSettings Callback to open the app's settings screen.
 */
@Composable
private fun NotificationPermissionPrompt(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val message = if (shouldShowRationale) {
        stringResource(R.string.notification_permission_rationale_message)
    } else {
        stringResource(R.string.notification_permission_denied_message)
    }

    val actionButtonText = if (shouldShowRationale) {
        stringResource(R.string.notification_permission_allow_button_text)
    } else {
        stringResource(R.string.notification_permission_settings_button_text)
    }

    val onClick = if (shouldShowRationale) onRequestPermission else onOpenSettings

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .padding(16.dp)
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        TextButton(
            modifier = Modifier
                .padding(top = 8.dp)
                .align(Alignment.End),
            onClick = onClick
        ) {
            Text(
                text = actionButtonText,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

/**
 * Preview parameter provider for displaying different notification permission states in previews.
 *
 * Provides sample values for the `shouldShowRationale` flag to simulate UI scenarios
 * where the user has either denied permission once or permanently.
 */
class ShouldShowRationaleProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(
            true,  // Should show rationale (e.g., user denied once)
            false  // Should go to app settings (e.g., denied permanently)
        )
}

/**
 * Preview of the [NotificationPermissionPrompt] composable with dynamic colors and support for light/dark themes.
 *
 * This preview allows visualization of the permission prompt in both rationale and permanent denial states.
 */
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun NotificationPermissionPromptPreview(
    @PreviewParameter(ShouldShowRationaleProvider::class)
    shouldShowRationale: Boolean,
) {
    GitHubNotifierTheme {
        NotificationPermissionPrompt(
            shouldShowRationale,
            onRequestPermission = {},
            onOpenSettings = {}
        )
    }
}
