package com.notifier.app.auth.presentation.login

import androidx.compose.runtime.Immutable

@Immutable
data class LoginState(
    val status: LoginStatus = LoginStatus.LOADING,
)

enum class LoginStatus {
    LOADING,
    LOGGED_IN,
    LOGGED_OUT,
}
