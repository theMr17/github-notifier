package com.notifier.app.auth.presentation.login

import androidx.compose.runtime.Immutable
import com.notifier.app.auth.domain.AuthToken

@Immutable
data class LoginState(
    val isLoading: Boolean = false,
    val authToken: AuthToken? = null,
)
