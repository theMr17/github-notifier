package com.notifier.app.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.notifier.app.auth.presentation.login.components.LoginButton
import com.notifier.app.ui.theme.GitHubNotifierTheme

@Composable
fun LoginScreen(
    state: AuthState,
    onAction: (AuthAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginButton()
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun LoginScreenPreview() {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            LoginScreen(
                state = AuthState(),
                onAction = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
