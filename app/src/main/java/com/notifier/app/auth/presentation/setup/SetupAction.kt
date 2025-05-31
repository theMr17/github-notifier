package com.notifier.app.auth.presentation.setup

/**
 * A sealed interface representing different user actions in the Setup screen.
 *
 * These actions are dispatched based on user interactions during the setup process.
 */
sealed interface SetupAction {
    /**
     * Action triggered when the user clicks the "Continue" button
     * after a successful setup.
     * This action will proceed the user to the next screen.
     */
    data object OnContinueButtonClick : SetupAction
}
