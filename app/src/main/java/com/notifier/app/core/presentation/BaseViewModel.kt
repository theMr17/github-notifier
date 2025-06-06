package com.notifier.app.core.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * A generic base [ViewModel] that handles UI state, user actions, and one-time events
 * in a structured and reactive manner using Kotlin's coroutines and Flow APIs.
 *
 * @param State The type representing the UI state.
 * @param Event The type representing one-time events (e.g., navigation, dialogs).
 * @param Action The type representing user actions or intents.
 * @param initialState The initial state for the screen.
 */
abstract class BaseViewModel<State, Event, Action>(
    initialState: State,
) : ViewModel() {

    /**
     * Internal mutable state holder, updated by the ViewModel.
     */
    protected val mutableStateFlow = MutableStateFlow(initialState)

    /**
     * Exposed immutable [StateFlow] that represents the current UI state.
     *
     * The state is kept active while there are active subscribers. It automatically
     * stops collecting updates after 5 seconds of inactivity to conserve resources.
     */
    open val state: StateFlow<State>
        get() = mutableStateFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = mutableStateFlow.value
            )

    /**
     * A buffered [Channel] used to send one-time [Event]s such as navigation or showing error
     * messages.
     */
    private val _events = Channel<Event>(Channel.BUFFERED)

    /**
     * A [Flow] that emits one-time [Event]s to be consumed by the UI.
     *
     * This should be collected using a lifecycle-aware collector (e.g., in a Compose
     * LaunchedEffect or Fragment).
     */
    val events: Flow<Event> = _events.receiveAsFlow()

    /**
     * Called when an [Action] is dispatched from the UI.
     *
     * Implementing ViewModels should override this to handle business logic based on the action.
     */
    abstract fun onAction(action: Action)

    /**
     * Sends a one-time [Event] to the UI.
     *
     * This is useful for things like navigation, showing snack bars, or triggering dialogs.
     * This method launches a coroutine within the [viewModelScope].
     *
     * @param event The event to send to the UI.
     */
    protected fun sendEvent(event: Event) {
        viewModelScope.launch { _events.send(event) }
    }
}
