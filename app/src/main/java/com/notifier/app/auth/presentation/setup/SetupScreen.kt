package com.notifier.app.auth.presentation.setup

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
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.notifier.app.ui.theme.GitHubNotifierTheme

/**
 * A composable function that displays the GitHub setup screen UI.
 *
 * Based on the current [SetupState], this screen provides visual feedback to the user during
 * different stages of GitHub token setup:
 * - **FETCHING_TOKEN**: Indicates that the app is connecting to GitHub.
 * - **SAVING_TOKEN**: Indicates that the token is being stored locally.
 * - **SUCCESS**: Indicates a successful connection and provides a Continue button.
 * - **FAILED**: Indicates that the setup failed and instructs the user to retry.
 *
 * @param state The current setup state that determines which UI is shown.
 * @param onAction A callback triggered when the user interacts with the screen (e.g., clicking Continue).
 * @param modifier An optional [Modifier] to be applied to the root layout.
 */
@Composable
fun SetupScreen(
    state: SetupState,
    onAction: (SetupAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (state.setupStep) {
            SetupStep.FETCHING_TOKEN -> {
                Text(text = "Connecting to GitHub...")
            }

            SetupStep.SAVING_TOKEN -> {
                Text(text = "Saving user information...")
            }

            SetupStep.SUCCESS -> {
                Text(text = "Connected successfully!")
                Button(
                    onClick = { onAction(SetupAction.OnContinueButtonClick) },
                ) {
                    Text(text = "Continue")
                }
            }

            SetupStep.FAILED -> {
                Text(text = "Connection failed. Please try again.")
            }
        }
    }
}

/**
 * Preview parameter provider for displaying different setup states in previews.
 *
 * Provides sample values for the [SetupState] to simulate each setup step (fetching, saving, success, failed).
 */
class SetupStateParameterProvider : PreviewParameterProvider<SetupState> {
    override val values: Sequence<SetupState>
        get() = sequenceOf(
            SetupState(setupStep = SetupStep.FETCHING_TOKEN),
            SetupState(setupStep = SetupStep.SAVING_TOKEN),
            SetupState(setupStep = SetupStep.SUCCESS),
            SetupState(setupStep = SetupStep.FAILED)
        )
}

/**
 * Preview of the [SetupScreen] composable with dynamic colors and light/dark theme support.
 *
 * This preview helps visualize how the Setup screen looks in different visual states.
 */
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun SetupScreenPreview(
    @PreviewParameter(SetupStateParameterProvider::class) state: SetupState,
) {
    GitHubNotifierTheme {
        Scaffold { innerPadding ->
            SetupScreen(
                state = state,
                onAction = {},
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

