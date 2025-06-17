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

@Composable
fun WithNotificationPermission(
    content: @Composable () -> Unit,
) {
    Column {
        NotificationPermissionHandler()
        content()
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun NotificationPermissionHandler() {
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
        NotificationPermissionPrompt (
            shouldShowRationale = permissionState.status.shouldShowRationale,
            onRequestPermission = {
                hasRequested = true
                permissionState.launchPermissionRequest()
            },
            onOpenSettings = {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts(
                        /* scheme = */ "package",
                        /* ssp = */ context.packageName,
                        /* fragment = */ null
                    )
                )
                context.startActivity(intent)
            }
        )
    }
}


@Composable
fun NotificationPermissionPrompt(
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

class ShouldShowRationaleProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(
            true,  // Should show rationale (e.g., user denied once)
            false  // Should go to app settings (e.g., denied permanently)
        )
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun NotificationPermissionPromptPreview(
    @PreviewParameter(ShouldShowRationaleProvider::class)
    shouldShowRationale: Boolean
) {
    GitHubNotifierTheme {
        NotificationPermissionPrompt(
            shouldShowRationale,
            onRequestPermission = {},
            onOpenSettings = {}
        )
    }
}
