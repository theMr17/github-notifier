package com.notifier.app.auth.presentation.setup

import androidx.compose.runtime.Immutable
import com.notifier.app.auth.domain.AuthToken

/**
 * UI state for the setup screen.
 *
 * Represents the current status of the initial setup process, such as fetching and saving the access token.
 *
 * @property setupStep The current step in the setup process, as represented by [SetupStep].
 * @property authToken The [AuthToken] retrieved during setup. Can be null if not yet fetched or if setup failed.
 */
@Immutable
data class SetupState(
    val setupStep: SetupStep = SetupStep.FETCHING_TOKEN,
    val authToken: AuthToken? = null,
)

/**
 * Enum representing the different stages of the setup process.
 */
enum class SetupStep {
    /** Currently retrieving the access token from GitHub. */
    FETCHING_TOKEN,

    /** Access token has been retrieved and is now being saved locally. */
    SAVING_TOKEN,

    /** Token has been saved successfully and setup is complete. */
    SUCCESS,

    /** An error occurred during the setup process. */
    FAILED
}
