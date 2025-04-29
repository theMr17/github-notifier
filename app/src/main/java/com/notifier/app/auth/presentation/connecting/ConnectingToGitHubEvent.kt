package com.notifier.app.auth.presentation.connecting

import com.notifier.app.core.domain.util.NetworkError

sealed interface ConnectingToGitHubEvent {
    data class Error(val error: NetworkError) : ConnectingToGitHubEvent
}
