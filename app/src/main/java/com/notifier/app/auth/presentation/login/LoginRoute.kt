package com.notifier.app.auth.presentation.login

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.notifier.app.core.presentation.util.ObserveAsEvents
import com.notifier.app.core.presentation.util.toString
import kotlinx.serialization.Serializable

@Serializable
data object LoginScreen

@Composable
fun LoginRoute(
    viewModel : LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    ObserveAsEvents(events = viewModel.events) { event ->
        when (event) {
            is LoginEvent.Error -> {
                Toast.makeText(
                    context,
                    event.error.toString(context),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    LoginScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) }
    )
}

