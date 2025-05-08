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

@HiltViewModel
class SetupViewModel @Inject constructor(
    private val authTokenDataSource: AuthTokenDataSource,
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {

    private val _state = MutableStateFlow(SetupState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
        initialValue = SetupState()
    )

    private val _events = Channel<SetupEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SetupAction) {
        when (action) {
            is SetupAction.OnContinueButtonClick -> navigateToHome()
        }
    }

    /**
     * Handles the OAuth redirect by verifying state and requesting the access token.
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
     * Navigates to the home screen.
     */
    private fun navigateToHome() {
        viewModelScope.launch {
            _events.send(SetupEvent.NavigateToHomeScreen)
        }
    }

    /**
     * Updates the current setup step.
     */
    private fun updateSetupStep(step: SetupStep) {
        _state.update { it.copy(setupStep = step) }
    }

    /**
     * Sets state to failed and stops the setup process.
     */
    private fun failSetup() {
        updateSetupStep(SetupStep.FAILED)
    }

    /**
     * Emits a network error event.
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
     * Emits a persistence error event.
     */
    private suspend fun handlePersistenceError(error: Error) {
        val actualError = if (error is PersistenceError) error else PersistenceError.UNKNOWN
        _events.send(SetupEvent.PersistenceErrorEvent(actualError))
    }
}
