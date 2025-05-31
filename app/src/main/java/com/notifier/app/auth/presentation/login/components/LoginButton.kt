package com.notifier.app.auth.presentation.login.components

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewDynamicColors
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.notifier.app.R
import com.notifier.app.ui.theme.GitHubNotifierTheme

/**
 * A composable button that triggers the GitHub login flow when clicked.
 *
 * @param onClick Lambda function to handle the button click event.
 * @param modifier Modifier to be applied to the button, allowing customization of its layout.
 */
@Composable
fun LoginButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        modifier = modifier,
        onClick = onClick,
    ) {
        Text(stringResource(R.string.login_button_text))
    }
}

/**
 * Preview function for the LoginButton composable in both light and dark modes with dynamic colors.
 */
@PreviewLightDark
@PreviewDynamicColors
@Composable
private fun LoginButtonPreview() {
    GitHubNotifierTheme {
        LoginButton(onClick = {})
    }
}
