package com.notifier.app.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.notifier.app.auth.presentation.login.components.LoginButton
import com.notifier.app.ui.theme.GitHubNotifierTheme

@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.status) {
            LoginStatus.LOADING -> {
                CircularProgressIndicator()
                Text(text = "Verifying authentication status...")
            }

            LoginStatus.LOGGED_IN -> {
                onAction(LoginAction.OnUserLoggedIn)
            }

            LoginStatus.LOGGED_OUT -> {
                LoginButton(
                    onClick = {
                        onAction(LoginAction.OnLoginButtonClick)
                    }
                )
            }
        }
    }
}

@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun LoginScreenPreview() {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            LoginScreen(
                state = LoginState(),
                onAction = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
