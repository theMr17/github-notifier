package com.notifier.app.auth.presentation.setup

import androidx.lifecycle.viewModelScope
import com.notifier.app.BuildConfig
import com.notifier.app.auth.domain.AuthTokenDataSource
import com.notifier.app.core.data.persistence.DataStoreManager
import com.notifier.app.core.domain.util.Error
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.PersistenceError
import com.notifier.app.core.domain.util.onError
import com.notifier.app.core.domain.util.onSuccess
import com.notifier.app.core.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
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
) : BaseViewModel<SetupState, SetupEvent, SetupAction>(SetupState()) {
    /**
     * Public immutable state exposed to UI.
     *
     * Emits the current [SetupState] to the UI, which reflects progress through setup steps.
     */
    override val state = mutableStateFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = SetupState()
    )

    /**
     * Handles user actions from the UI.
     *
     * @param action the user-triggered action
     */
    override fun onAction(action: SetupAction) {
        when (action) {
            is SetupAction.OnContinueButtonClick -> navigateToHome()
        }
    }

    /**
     * Handles the OAuth redirect by validating state and exchanging the authorization code
     * for an access token.
     *
     * If validation or token exchange fails, updates [mutableStateFlow] and emits error events.
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
                mutableStateFlow.update { it.copy(authToken = authToken) }
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
        sendEvent(SetupEvent.NavigateToHomeScreen)
    }

    /**
     * Updates the current setup step in [mutableStateFlow].
     *
     * @param step the new setup step
     */
    private fun updateSetupStep(step: SetupStep) {
        mutableStateFlow.update { it.copy(setupStep = step) }
    }

    /**
     * Marks the setup process as failed.
     *
     * Updates [mutableStateFlow] and prevents further progress.
     */
    private fun failSetup() {
        updateSetupStep(SetupStep.FAILED)
    }

    /**
     * Emits a [SetupEvent.NetworkErrorEvent] and marks setup as failed.
     *
     * @param error the network error that occurred
     */
    private fun handleNetworkError(error: Error) {
        failSetup()
        sendEvent(
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
    private fun handlePersistenceError(error: Error) {
        val actualError = if (error is PersistenceError) error else PersistenceError.UNKNOWN
        sendEvent(SetupEvent.PersistenceErrorEvent(actualError))
    }
}
