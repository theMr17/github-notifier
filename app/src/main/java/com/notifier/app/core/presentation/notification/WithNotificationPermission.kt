package com.notifier.app.core.presentation.notification

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log
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
import com.notifier.app.R
import com.notifier.app.ui.theme.GitHubNotifierTheme

/**
 * Wraps a screen or composable with logic to check and request notification permission on
 * Android 13+.
 *
 * If the permission is not granted, it will prompt the user appropriately—either by showing a
 * system dialog or a fallback message guiding them to enable it manually from settings.
 *
 * @param permissionState The [NotificationPermissionState] to use, primarily for testability.
 * If null, the default state from [rememberNotificationPermissionState] is used.
 * @param content The main content to be displayed alongside the permission prompt.
 */
@Composable
fun WithNotificationPermission(
    permissionState: NotificationPermissionState? = null,
    content: @Composable () -> Unit,
) {
    Column {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            NotificationPermissionHandler(
                permissionState = permissionState ?: rememberNotificationPermissionState()
            )
        }
        content()
    }
}

/**
 * Displays the appropriate notification permission prompt if the permission isn't granted.
 *
 * - If permission can still be requested, it shows a rationale and a retry button.
 * - If permanently denied, it shows a message guiding the user to system settings.
 *
 * @param permissionState Current state of the notification permission.
 */
@Composable
private fun NotificationPermissionHandler(
    permissionState: NotificationPermissionState,
) {
    val context = LocalContext.current
    var hasRequested by rememberSaveable { mutableStateOf(false) }

    LaunchedEffect(permissionState.isGranted, permissionState.shouldShowRationale) {
        if (!hasRequested && !permissionState.isGranted && !permissionState.shouldShowRationale) {
            hasRequested = true
            permissionState.requestPermission()
        }
    }

    if (!permissionState.isGranted) {
        NotificationPermissionPrompt(
            shouldShowRationale = permissionState.shouldShowRationale,
            onRequestPermission = {
                hasRequested = true
                permissionState.requestPermission()
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
                }.onFailure { throwable ->
                    Log.e(
                        "WithNotificationPermission",
                        "Unable to open app settings",
                        throwable
                    )
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
