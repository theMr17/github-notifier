package com.notifier.app.auth.presentation.login

sealed interface LoginAction {
    data object OnLoginButtonClick : LoginAction
}
