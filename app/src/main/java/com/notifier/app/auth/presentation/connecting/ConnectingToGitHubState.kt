package com.notifier.app.auth.presentation.connecting

import androidx.compose.runtime.Immutable
import com.notifier.app.auth.domain.AuthToken

@Immutable
data class ConnectingToGitHubState(
    val connectionState: ConnectionState = ConnectionState.FETCHING_TOKEN,
    val authToken: AuthToken? = null,
)

enum class ConnectionState {
    FETCHING_TOKEN,
    SAVING_TOKEN,
    SUCCESS,
    FAILED,
}
