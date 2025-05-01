package com.notifier.app.auth.presentation.setup

import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.PersistenceError

sealed interface SetupEvent {
    data class NetworkErrorEvent(val error: NetworkError) : SetupEvent
    data class PersistenceErrorEvent(val error: PersistenceError) : SetupEvent
}
