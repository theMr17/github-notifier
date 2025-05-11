package com.notifier.app.auth.presentation.setup

import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.PersistenceError

/**
 * A sealed interface representing different setup-related events.
 *
 * These events are used to trigger UI actions or navigation in response to state changes
 * during the setup process.
 */
sealed interface SetupEvent {
    /**
     * Event that triggers a toast or error message for a network-related error.
     *
     * This event is fired when a network error occurs while fetching or validating data.
     *
     * @param error The network error that occurred.
     */
    data class NetworkErrorEvent(val error: NetworkError) : SetupEvent

    /**
     * Event that triggers a toast or error message for a persistence-related error.
     *
     * This event is fired when an error occurs while saving data locally.
     *
     * @param error The persistence error that occurred.
     */
    data class PersistenceErrorEvent(val error: PersistenceError) : SetupEvent

    /**
     * Event that triggers navigation to the home screen.
     *
     * This event is fired after the setup process completes successfully and the user
     * should be redirected to the home screen.
     */
    data object NavigateToHomeScreen : SetupEvent
}
