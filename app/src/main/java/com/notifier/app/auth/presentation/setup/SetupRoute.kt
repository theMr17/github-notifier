package com.notifier.app.auth.presentation.setup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notifier.app.core.presentation.util.ObserveAsEvents
import com.notifier.app.core.presentation.util.showToast
import com.notifier.app.core.presentation.util.toString
import kotlinx.serialization.Serializable

/**
 * Navigation route object for the Setup screen.
 *
 * This data class holds parameters required for the Setup screen to function,
 * such as the GitHub authorization code and state.
 * It is used for deep linking and navigation within the app.
 *
 * @property code The authorization code returned by GitHub after successful login.
 * @property state A value to protect against CSRF attacks and validate the response.
 */
@Serializable
data class SetupScreen(
    val code: String? = null,
    val state: String? = null,
)

/**
 * The SetupRoute Composable is the entry point to the Setup screen.
 *
 * It initializes the access token retrieval process using the provided authorization code,
 * observes the setup state from the [SetupViewModel], handles side effects such as error toasts and
 * navigation, and renders the Setup screen.
 *
 * @param code The authorization code received from GitHub.
 * @param receivedState The state parameter received from GitHub for CSRF protection.
 * @param onNavigateToHomeScreen A callback invoked when setup completes successfully.
 * @param viewModel The instance of the [SetupViewModel] used for managing setup logic and state.
 */
@Composable
fun SetupRoute(
    code: String?,
    receivedState: String?,
    onNavigateToHomeScreen: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Starts token retrieval when the authorization code is available
    LaunchedEffect(code) {
        viewModel.getAuthToken(code, receivedState)
    }

    // Observes events and performs UI side effects such as showing toasts or navigating
    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is SetupEvent.PersistenceErrorEvent -> {
                showToast(context, event.error.toString(context))
            }
            is SetupEvent.NetworkErrorEvent -> {
                showToast(context, event.error.toString(context))
            }
            SetupEvent.NavigateToHomeScreen -> {
                onNavigateToHomeScreen()
            }
        }
    }

    // Renders the Setup screen with the current UI state
    SetupScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) }
    )
}
