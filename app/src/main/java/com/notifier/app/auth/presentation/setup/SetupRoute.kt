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

@Serializable
data class SetupScreen(
    val code: String? = null,
    val state: String? = null,
)

@Composable
fun SetupRoute(
    code: String?,
    onNavigateToHomeScreen: () -> Unit,
    viewModel: SetupViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(code) {
        viewModel.getAuthToken(code)
    }

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

    SetupScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) }
    )
}
