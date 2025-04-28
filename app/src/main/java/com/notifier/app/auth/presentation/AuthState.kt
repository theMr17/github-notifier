package com.notifier.app.auth.presentation

import androidx.compose.runtime.Immutable
import com.notifier.app.auth.domain.AuthToken

@Immutable
data class AuthState(
    val isLoading: Boolean = false,
    val authToken: AuthToken? = null,
)
