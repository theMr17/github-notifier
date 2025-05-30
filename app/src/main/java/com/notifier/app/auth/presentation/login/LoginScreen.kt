package com.notifier.app.auth.presentation.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.notifier.app.auth.presentation.login.components.LoginButton
import com.notifier.app.ui.theme.GitHubNotifierTheme

const val TEST_TAG_CIRCULAR_PROGRESS_INDICATOR = "TestTag.CircularProgressIndicator"

/**
 * A composable function that displays the login screen UI.
 *
 * This function renders different views based on the login status:
 * - **Loading**: Displays a circular progress indicator and a status message.
 * - **Logged Out**: Displays a login button.
 * - **Logged In**: Displays a success message.
 *
 * @param state The current login state that dictates what UI is shown.
 * @param onLoginButtonClick The action to take when the login button is clicked.
 * @param modifier An optional modifier to be applied to the root layout.
 */
@Composable
fun LoginScreen(
    state: LoginState,
    onLoginButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.status) {
            LoginStatus.LOADING, null -> {
                CircularProgressIndicator(
                    modifier = Modifier.testTag(TEST_TAG_CIRCULAR_PROGRESS_INDICATOR),
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text("Verifying authentication status...")
            }

            LoginStatus.LOGGED_OUT -> {
                LoginButton(onClick = onLoginButtonClick)
            }

            LoginStatus.LOGGED_IN -> {
                Text("Logged in successfully! Redirecting...")
            }
        }
    }
}

/**
 * Preview parameter provider for displaying different login states in previews.
 *
 * Provides sample values for the LoginState to simulate different UI scenarios.
 */
class LoginStateParameterProvider : PreviewParameterProvider<LoginState> {
    override val values: Sequence<LoginState>
        get() = sequenceOf(
            LoginState(status = LoginStatus.LOADING),
            LoginState(status = LoginStatus.LOGGED_OUT),
            LoginState(status = LoginStatus.LOGGED_IN)
        )
}

/**
 * Preview of the [LoginScreen] composable with dynamic colors and support for light/dark themes.
 *
 * This preview allows visualization of the login screen in various states (loading, logged out, logged in).
 */
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun LoginScreenPreview(
    @PreviewParameter(LoginStateParameterProvider::class) state: LoginState,
) {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            LoginScreen(
                state = state,
                onLoginButtonClick = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}
