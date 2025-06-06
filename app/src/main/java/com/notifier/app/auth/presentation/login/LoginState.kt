package com.notifier.app.auth.presentation.login

import androidx.compose.runtime.Immutable

/**
 * UI state for the login screen.
 *
 * Represents the current login status displayed to the user.
 *
 * @property status The [LoginStatus] representing whether the user is:
 * - Loading authentication check
 * - Logged in
 * - Logged out
 * Can be null initially before the status is determined.
 */
@Immutable
data class LoginState(
    val status: LoginStatus? = null,
)

/**
 * Enum representing the possible authentication statuses for the login screen.
 */
enum class LoginStatus {
    /** Indicates authentication status is being checked. */
    LOADING,

    /** Indicates user is authenticated and logged in. */
    LOGGED_IN,

    /** Indicates user is not authenticated and needs to log in. */
    LOGGED_OUT,
}
