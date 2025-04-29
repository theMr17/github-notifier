package com.notifier.app.auth.presentation.connecting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notifier.app.BuildConfig
import com.notifier.app.auth.domain.AuthTokenDataSource
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
class ConnectingToGitHubViewModel @Inject constructor(
    private val authTokenDataSource: AuthTokenDataSource,
) : ViewModel() {
    private val _state = MutableStateFlow(ConnectingToGitHubState())
    val state = _state
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            ConnectingToGitHubState()
        )

    private val _events = Channel<ConnectingToGitHubEvent>()
    val events = _events.receiveAsFlow()

    fun getAuthToken(code: String?) {
        if (code.isNullOrBlank()) {
            _state.update {
                it.copy(
                    connectionState = ConnectionState.FAILED
                )
            }
            return
        }

        _state.update {
            it.copy(
                connectionState = ConnectionState.FETCHING_TOKEN
            )
        }

        viewModelScope.launch {
            authTokenDataSource.getAuthToken(
                clientId = BuildConfig.CLIENT_ID,
                clientSecret = BuildConfig.CLIENT_SECRET,
                code = code
            ).onSuccess { authToken ->
                _state.update {
                    it.copy(
                        connectionState = ConnectionState.SAVING_TOKEN,
                        authToken = authToken
                    )
                }
            }.onError { error ->
                _events.send(ConnectingToGitHubEvent.Error(error))
            }
        }
    }
}
