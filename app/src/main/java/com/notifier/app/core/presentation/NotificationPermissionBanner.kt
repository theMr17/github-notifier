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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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

    LaunchedEffect(Unit) {
        if (!permissionState.status.isGranted && !permissionState.status.shouldShowRationale) {
            permissionState.launchPermissionRequest()
        }
    }

    if (!permissionState.status.isGranted) {
        NotificationPermissionBanner(
            shouldShowRationale = permissionState.status.shouldShowRationale,
            onRequestPermission = { permissionState.launchPermissionRequest() },
            onOpenSettings = {
                val intent = Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
                context.startActivity(intent)
            }
        )
    }
}

@Composable
fun NotificationPermissionBanner(
    shouldShowRationale: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
) {
    val message = if (shouldShowRationale) {
        "To deliver GitHub notifications like pull requests, issues, and mentions, we need notification access. Please allow it."
    } else {
        "Notification access is required to show updates from your GitHub activity. Enable it from system settings."
    }

    val actionButtonText = if (shouldShowRationale) {
        "Allow Notifications"
    } else {
        "Open Settings"
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

class NotificationPermissionPreviewProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean>
        get() = sequenceOf(
            true,  // Should show rationale (e.g., user denied once)
            false  // Should go to app settings (e.g., denied permanently)
        )
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun NotificationPermissionBannerPreview(
    @PreviewParameter(NotificationPermissionPreviewProvider::class)
    shouldShowRationale: Boolean
) {
    GitHubNotifierTheme {
        NotificationPermissionBanner(
            shouldShowRationale,
            onRequestPermission = {},
            onOpenSettings = {}
        )
    }
}
