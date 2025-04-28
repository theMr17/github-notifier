package com.notifier.app.auth.presentation

sealed interface AuthAction {
    data object OnLoginButtonClick : AuthAction
}
