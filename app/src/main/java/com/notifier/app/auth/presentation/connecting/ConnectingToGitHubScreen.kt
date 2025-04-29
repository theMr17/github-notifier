package com.notifier.app.auth.presentation.connecting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
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
    state: ConnectingToGitHubState,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.connectionState) {
            ConnectionState.FETCHING_TOKEN -> {
                Text(text = "Connecting to GitHub...")
            }
            ConnectionState.SAVING_TOKEN -> {
                Text(text = "Saving user information...")
            }
            ConnectionState.SUCCESS -> {
                Text(text = "Connected successfully!")
                Button(
                    onClick = { /* TODO: Navigate to the next screen */ },
                ) {
                    Text(text = "Continue")
                }
            }
            ConnectionState.FAILED -> {
                Text(text = "Connection Failed. Please try again.")
            }
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun ConnectingToGitHubScreenPreview() {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            ConnectingToGitHubScreen(
                state = ConnectingToGitHubState(),
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
