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
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state
        .onStart { checkAuthStatus() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            LoginState()
        )

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.OnLoginButtonClick -> {
                viewModelScope.launch {
                    _events.send(LoginEvent.NavigateToGitHubAuth)
                }
            }

            LoginAction.OnUserLoggedIn -> {
                viewModelScope.launch {
                    _events.send(LoginEvent.NavigateToHomeScreen)
                }
            }
        }
    }

    private fun checkAuthStatus() {
        viewModelScope.launch {
            dataStoreManager.getAccessToken().onSuccess { accessToken ->
                _state.update {
                    it.copy(
                        status = if (accessToken.isBlank())
                            LoginStatus.LOGGED_OUT
                        else
                            LoginStatus.LOGGED_IN
                    )
                }
            }.onError { error ->
                _state.update { it.copy(status = LoginStatus.LOGGED_OUT) }
                Log.d("LoginViewModel", "Error fetching access token: $error")
            }
        }
    }
}
