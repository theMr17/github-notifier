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

abstract class BaseViewModel<State, Event, Action>(
    private val initialState: State,
) : ViewModel() {
    protected val mutableStateFlow = MutableStateFlow(initialState)
    open val state: StateFlow<State>
        get() = mutableStateFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
                initialValue = initialState // Initial state before any flow emissions
            )

    private val _events = Channel<Event>(Channel.BUFFERED)
    val events: Flow<Event> = _events.receiveAsFlow()

    abstract fun onAction(action: Action)

    protected fun sendEvent(event: Event) {
        viewModelScope.launch { _events.send(event) }
    }
}
