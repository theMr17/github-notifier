package com.notifier.app.auth.presentation.login

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notifier.app.core.presentation.util.ObserveAsEvents
import kotlinx.serialization.Serializable

@Serializable
data object LoginScreen

@Composable
fun LoginRoute(
    onNavigateToHomeScreen: () -> Unit,
    onNavigateToGitHubAuth: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            LoginEvent.NavigateToHomeScreen -> {
                onNavigateToHomeScreen()
            }

            LoginEvent.NavigateToGitHubAuth -> {
                onNavigateToGitHubAuth()
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) }
    )
}

