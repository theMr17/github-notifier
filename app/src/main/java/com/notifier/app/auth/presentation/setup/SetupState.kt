package com.notifier.app.auth.presentation.setup

import androidx.compose.runtime.Immutable
import com.notifier.app.auth.domain.AuthToken

@Immutable
data class SetupState(
    val setupStep: SetupStep = SetupStep.FETCHING_TOKEN,
    val authToken: AuthToken? = null,
)

enum class SetupStep {
    /** Currently retrieving the access token from GitHub. */
    FETCHING_TOKEN,

    /** Access token has been retrieved, now saving locally. */
    SAVING_TOKEN,

    /** Token saved successfully and setup is complete. */
    SUCCESS,

    /** An error occurred during setup. */
    FAILED
}
