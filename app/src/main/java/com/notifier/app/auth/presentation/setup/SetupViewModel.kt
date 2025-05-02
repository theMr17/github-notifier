package com.notifier.app.auth.presentation.setup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifier.app.BuildConfig
import com.notifier.app.auth.domain.AuthTokenDataSource
import com.notifier.app.core.data.persistence.DataStoreManager
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
    val state = _state
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = SetupState()
        )

    private val _events = Channel<SetupEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SetupAction) {
        when (action) {
            is SetupAction.OnContinueButtonClick -> {
                viewModelScope.launch {
                    _events.send(SetupEvent.NavigateToHomeScreen)
                }
            }
        }
    }

    /**
     * Initiates the process of exchanging the authorization code for an access token.
     * Updates state accordingly and emits error events when necessary.
     */
    fun getAuthToken(code: String?) {
        if (code.isNullOrBlank()) {
            _state.update { it.copy(setupStep = SetupStep.FAILED) }
            return
        }

        _state.update { it.copy(setupStep = SetupStep.FETCHING_TOKEN) }

        viewModelScope.launch {
            authTokenDataSource.getAuthToken(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                code = code
            ).onSuccess { authToken ->
                _state.update {
                    it.copy(
                        setupStep = SetupStep.SAVING_TOKEN,
                        authToken = authToken
                    )
                }
                saveAuthToken(authToken.accessToken)
            }.onError { error ->
                _state.update { it.copy(setupStep = SetupStep.FAILED) }
                _events.send(SetupEvent.NetworkErrorEvent(error as NetworkError))
            }
        }
    }

    /**
     * Saves the access token to DataStore and updates setup state.
     * Emits an error event if saving fails.
     */
    private fun saveAuthToken(token: String) {
        viewModelScope.launch {
            dataStoreManager.setAccessToken(token).onSuccess {
                _state.update { it.copy(setupStep = SetupStep.SUCCESS) }
            }.onError { error ->
                _state.update { it.copy(setupStep = SetupStep.FAILED) }
                _events.send(SetupEvent.PersistenceErrorEvent(error as PersistenceError))
            }
        }
    }
}
