package com.notifier.app.auth.presentation.login

/**
 * A sealed interface representing different login-related events.
 *
 * These events are used to trigger UI navigation actions in response to user interactions or
 * state changes.
 */
sealed interface LoginEvent {
    /**
     * Event that triggers navigation to the home screen.
     *
     * This event is fired when the user successfully logs in and should be redirected to the
     * home screen.
     */
    data object NavigateToHomeScreen : LoginEvent

    /**
     * Event that triggers navigation to the GitHub authentication screen.
     *
     * This event is fired when the user presses the login button to authenticate via GitHub.
     */
    data object NavigateToGitHubAuth : LoginEvent
}
