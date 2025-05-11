package com.notifier.app.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notifier.app.core.presentation.util.ObserveAsEvents
import kotlinx.serialization.Serializable

/**
 * Navigation route object for the Login screen.
 *
 * This is used for identifying and serializing the Login screen destination.
 * It is used in conjunction with navigation libraries that require serializable objects.
 */
@Serializable
data object LoginScreen

/**
 * The LoginRoute Composable is the entry point to the Login screen.
 *
 * It observes the login state from the [LoginViewModel], listens for navigation events,
 * and renders the appropriate UI. Based on the current login status, the view model is triggered
 * to navigate to different screens, such as the GitHub authentication or home screen.
 *
 * @param onNavigateToHomeScreen A callback invoked when the user is successfully logged in.
 * @param onNavigateToGitHubAuth A callback invoked when the user needs to authenticate via GitHub.
 * @param viewModel The instance of the [LoginViewModel] used for managing login state and actions.
 */
@Composable
fun LoginRoute(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToGitHubAuth: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            LoginEvent.NavigateToHomeScreen -> onNavigateToHomeScreen()
            LoginEvent.NavigateToGitHubAuth -> onNavigateToGitHubAuth()
        }
    }

    // Triggered when the login status changes to LOGGED_IN, initiating navigation actions
    LaunchedEffect(state.status) {
        if (state.status == LoginStatus.LOGGED_IN) {
            // The user has logged in, so we trigger the OnUserLoggedIn action
            viewModel.onAction(LoginAction.OnUserLoggedIn)
        }
    }

    LoginScreen(
        state = state,
        onLoginButtonClick = { viewModel.onAction(LoginAction.OnLoginButtonClick) }
    )
}
