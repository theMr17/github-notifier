package com.notifier.app.auth.presentation.login

sealed interface LoginEvent {
    data object NavigateToHomeScreen : LoginEvent
    data object NavigateToGitHubAuth : LoginEvent
}
