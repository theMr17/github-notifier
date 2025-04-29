package com.notifier.app.auth.presentation.login

import com.notifier.app.core.domain.util.NetworkError

sealed interface AuthEvent {
    data class Error(val error: NetworkError) : AuthEvent
}
