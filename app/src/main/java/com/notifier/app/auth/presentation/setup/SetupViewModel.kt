package com.notifier.app.auth.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifier.app.BuildConfig
import com.notifier.app.auth.domain.AuthTokenDataSource
import com.notifier.app.core.data.persistence.DataStoreManager
import com.notifier.app.core.domain.util.Error
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.PersistenceError
import com.notifier.app.core.domain.util.onError
import com.notifier.app.core.domain.util.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing the setup screen's state and events.
 *
 * This ViewModel handles:
 * - Managing OAuth authentication flow (state validation, token retrieval, token persistence)
 * - Managing setup UI state (loading, success, failed)
 * - Sending navigation and error events to the UI
 *
 * @property authTokenDataSource Used to exchange the OAuth code for an access token.
 * @property dataStoreManager Used to store and retrieve OAuth-related data locally.
 */
@HiltViewModel
class SetupViewModel @Inject constructor(
    private val authTokenDataSource: AuthTokenDataSource,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    // Internal mutable state holding the current setup UI state
    private val _state = MutableStateFlow(SetupState())

    /**
     * Public immutable state exposed to UI.
     *
     * Emits the current [SetupState] to the UI, which reflects progress through setup steps.
     */
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = SetupState()
    )

    // Internal channel for emitting one-time UI events (e.g., navigation, error messages)
    private val _events = Channel<SetupEvent>()

    /**
     * Public flow of one-time events consumed by the UI (e.g., navigation triggers, error toasts).
     */
    val events = _events.receiveAsFlow()

    /**
     * Handles user actions from the UI.
     *
     * @param action the user-triggered action
     */
    fun onAction(action: SetupAction) {
        when (action) {
            is SetupAction.OnContinueButtonClick -> navigateToHome()
        }
    }

    /**
     * Handles the OAuth redirect by validating state and exchanging the authorization code
     * for an access token.
     *
     * If validation or token exchange fails, updates [_state] and emits error events.
     *
     * @param code the authorization code from OAuth callback
     * @param receivedState the OAuth state parameter from OAuth callback
     */
    fun getAuthToken(code: String?, receivedState: String?) {
        if (code.isNullOrBlank() || receivedState.isNullOrBlank()) {
            failSetup()
            return
        }

        viewModelScope.launch {
            val isStateValid = validateOAuthState(receivedState)
            if (!isStateValid) return@launch

            updateSetupStep(SetupStep.FETCHING_TOKEN)

            authTokenDataSource.getAuthToken(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                code = code
            ).onSuccess { authToken ->
                updateSetupStep(SetupStep.SAVING_TOKEN)
                _state.update { it.copy(authToken = authToken) }
                saveAuthToken(authToken.accessToken)
            }.onError { error ->
                handleNetworkError(error)
            }
        }
    }

    /**
     * Validates the received OAuth state against the saved state.
     *
     * @param receivedState the OAuth state received from OAuth callback
     * @return true if state is valid; false otherwise (also triggers setup failure)
     */
    private suspend fun validateOAuthState(receivedState: String): Boolean {
        var savedState = ""
        dataStoreManager.getOAuthState()
            .onSuccess { savedState = it }
            .onError {
                failSetup()
                return false
            }

        if (receivedState != savedState) {
            failSetup()
            return false
        }

        dataStoreManager.clearOAuthState()
            .onError {
                failSetup()
                return false
            }

        return true
    }

    /**
     * Saves the access token and updates setup state.
     *
     * Emits a [SetupEvent.PersistenceErrorEvent] if saving fails.
     *
     * @param token the OAuth access token to save
     */
    private fun saveAuthToken(token: String) {
        viewModelScope.launch {
            dataStoreManager.setAccessToken(token).onSuccess {
                updateSetupStep(SetupStep.SUCCESS)
            }.onError { error ->
                failSetup()
                handlePersistenceError(error)
            }
        }
    }

    /**
     * Sends an event to navigate to the home screen.
     */
    private fun navigateToHome() {
        viewModelScope.launch {
            _events.send(SetupEvent.NavigateToHomeScreen)
        }
    }

    /**
     * Updates the current setup step in [_state].
     *
     * @param step the new setup step
     */
    private fun updateSetupStep(step: SetupStep) {
        _state.update { it.copy(setupStep = step) }
    }

    /**
     * Marks the setup process as failed.
     *
     * Updates [_state] and prevents further progress.
     */
    private fun failSetup() {
        updateSetupStep(SetupStep.FAILED)
    }

    /**
     * Emits a [SetupEvent.NetworkErrorEvent] and marks setup as failed.
     *
     * @param error the network error that occurred
     */
    private suspend fun handleNetworkError(error: Error) {
        failSetup()
        _events.send(
            SetupEvent.NetworkErrorEvent(
                if (error is NetworkError) error else NetworkError.UNKNOWN
            )
        )
    }

    /**
     * Emits a [SetupEvent.PersistenceErrorEvent] when a persistence error occurs.
     *
     * @param error the persistence error that occurred
     */
    private suspend fun handlePersistenceError(error: Error) {
        val actualError = if (error is PersistenceError) error else PersistenceError.UNKNOWN
        _events.send(SetupEvent.PersistenceErrorEvent(actualError))
    }
}
