package com.notifier.app.auth.presentation.setup

sealed interface SetupAction {
    data object OnContinueButtonClick : SetupAction
}
