package com.notifier.app.auth.presentation.login.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.notifier.app.ui.theme.GitHubNotifierTheme

@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text("Login with GitHub")
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun LoginButtonPreview() {
    GitHubNotifierTheme {
        LoginButton(onClick = {})
    }
}
