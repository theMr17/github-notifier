package com.notifier.app.auth.presentation.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.notifier.app.auth.presentation.util.createGitHubAuthIntent
import com.notifier.app.ui.theme.GitHubNotifierTheme

@Composable
fun LoginButton(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Button(
        modifier = modifier,
        onClick = {
            context.startActivity(createGitHubAuthIntent())
        },
    ) {
        Text("Login with GitHub")
    }
}

@Preview
@Composable
private fun LoginButtonPreview() {
    GitHubNotifierTheme {
        LoginButton()
    }
}
