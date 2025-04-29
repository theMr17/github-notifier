package com.notifier.app.auth.presentation.connecting

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notifier.app.core.presentation.util.ObserveAsEvents
import com.notifier.app.core.presentation.util.toString
import kotlinx.serialization.Serializable

@Serializable
data class ConnectingToGitHubScreen(
    val code: String? = null,
    val state: String? = null,
)

@Composable
fun ConnectingToGitHubRoute(
    code: String?,
    viewModel: ConnectingToGitHubViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(code) {
        viewModel.getAuthToken(code)
    }

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is ConnectingToGitHubEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    ConnectingToGitHubScreen(state = state)
}
