package com.notifier.app.auth.presentation.login

sealed interface AuthAction {
    data object OnLoginButtonClick : AuthAction
}
