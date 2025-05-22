package com.notifier.app.auth.presentation.login

/**
 * A sealed interface representing different actions related to the login process.
 *
 * These actions are used to trigger changes in the state of the login flow based on user
 * interactions or other events.
 */
sealed interface LoginAction {
    /**
     * Action triggered when the login button is clicked.
     * This action will initiate the login process.
     */
    data object OnLoginButtonClick : LoginAction

    /**
     * Action triggered when the user is successfully logged in.
     * This action will mark the login process as completed.
     */
    data object OnUserLoggedIn : LoginAction
}
