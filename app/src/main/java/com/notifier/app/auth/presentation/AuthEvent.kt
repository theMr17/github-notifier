package com.notifier.app.auth.presentation

import com.notifier.app.core.domain.util.NetworkError

sealed interface AuthEvent {
    data class Error(val error: NetworkError) : AuthEvent
}
