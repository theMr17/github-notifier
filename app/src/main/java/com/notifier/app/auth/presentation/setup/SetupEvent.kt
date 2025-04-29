package com.notifier.app.auth.presentation.setup

import com.notifier.app.core.domain.util.NetworkError

sealed interface SetupEvent {
    data class Error(val error: NetworkError) : SetupEvent
}
