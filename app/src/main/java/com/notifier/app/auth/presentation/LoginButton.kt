package com.notifier.app.auth.presentation

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.notifier.app.ui.theme.GitHubNotifierTheme

@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text("Login with GitHub")
    }
}

@Preview
@Composable
private fun LoginButtonPreview() {
    GitHubNotifierTheme {
        LoginButton(onClick = {})
    }
}
