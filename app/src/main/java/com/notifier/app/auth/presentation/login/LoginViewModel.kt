package com.notifier.app.auth.presentation.login

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifier.app.core.data.persistence.DataStoreManager
import com.notifier.app.core.domain.util.onError
import com.notifier.app.core.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the login screen's state and events.
 *
 * This ViewModel handles:
 * - Checking if the user is already authenticated
 * - Managing login UI state (loading, logged in, logged out)
 * - Sending navigation events to the UI
 *
 * @property dataStoreManager Used to retrieve the stored access token for authentication check.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    companion object {
        /** Tag used for logging errors from this ViewModel. */
        private const val TAG = "LoginViewModel"
    }

    // Internal mutable state holding the current login UI state
    private val _state = MutableStateFlow(LoginState())

    /**
     * Public immutable state exposed to UI.
     *
     * - Triggers [checkAuthStatus] when the flow starts collecting (via [onStart])
     * - Emits the current [LoginState] to the UI
     */
    val state: StateFlow<LoginState> = _state
        .onStart {
            // Mark the UI as loading while checking authentication status
            _state.update { it.copy(status = LoginStatus.LOADING) }
            checkAuthStatus()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = LoginState() // Initial state before any flow emissions
        )

    // Internal channel for emitting one-time UI events (e.g., navigation)
    private val _events = Channel<LoginEvent>(Channel.BUFFERED)

    /**
     * Public flow of one-time events consumed by the UI (e.g., navigation triggers).
     */
    val events = _events.receiveAsFlow()

    /**
     * Handles user actions from the UI.
     *
     * @param action the user-triggered action
     */
    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLoginButtonClick -> sendEvent(LoginEvent.NavigateToGitHubAuth)
            LoginAction.OnUserLoggedIn -> sendEvent(LoginEvent.NavigateToHomeScreen)
        }
    }

    /**
     * Checks if the user is already authenticated by retrieving the saved access token.
     *
     * Updates [_state] based on whether a valid token is found:
     * - [LoginStatus.LOGGED_IN] if a non-blank token exists
     * - [LoginStatus.LOGGED_OUT] otherwise
     *
     * If an error occurs, logs the error and treats user as logged out.
     */
    private fun checkAuthStatus() {
        viewModelScope.launch {
            dataStoreManager.getAccessToken()
                .onSuccess { accessToken ->
                    _state.update {
                        it.copy(
                            status = if (accessToken.isBlank()) LoginStatus.LOGGED_OUT
                            else LoginStatus.LOGGED_IN
                        )
                    }
                }
                .onError { error ->
                    Log.e(TAG, "Error fetching access token: $error")
                    _state.update { it.copy(status = LoginStatus.LOGGED_OUT) }
                }
        }
    }

    /**
     * Sends a one-time event to the UI.
     *
     * @param event the event to send (e.g., navigation trigger)
     */
    private fun sendEvent(event: LoginEvent) {
        viewModelScope.launch { _events.send(event) }
    }
}
