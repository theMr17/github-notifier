package com.notifier.app.auth.presentation.connecting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.notifier.app.ui.theme.GitHubNotifierTheme

@Composable
fun ConnectingToGitHubScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Connecting to GitHub...",
        )
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun ConnectingToGitHubScreenPreview() {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            ConnectingToGitHubScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
