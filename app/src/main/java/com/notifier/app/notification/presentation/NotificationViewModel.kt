package com.notifier.app.notification.presentation

import androidx.lifecycle.viewModelScope
import com.notifier.app.core.domain.util.NetworkError
import com.notifier.app.core.domain.util.onError
import com.notifier.app.core.domain.util.onSuccess
import com.notifier.app.core.presentation.BaseViewModel
import com.notifier.app.notification.domain.NotificationDataSource
import com.notifier.app.notification.presentation.model.toNotificationUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : BaseViewModel<NotificationState, NotificationEvent, NotificationAction>(NotificationState()) {
    override val state: StateFlow<NotificationState> = mutableStateFlow
        .onStart {
            mutableStateFlow.update { it.copy(isLoading = true) }
            getNotifications()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000L),
            initialValue = NotificationState()
        )

    override fun onAction(action: NotificationAction) {
        when (action) {
            is NotificationAction.OnNotificationItemClick -> {
                // Handle notification item click, e.g., navigate to details
            }
        }
    }

    private fun getNotifications() {
        viewModelScope.launch {
            notificationDataSource.getNotifications()
                .onSuccess { notifications ->
                    mutableStateFlow.update {
                        it.copy(
                            notifications = notifications.map { notification ->
                                notification.toNotificationUi()
                            },
                            isLoading = false
                        )
                    }
                }
                .onError { error ->
                    mutableStateFlow.update { it.copy(isLoading = false) }
                    sendEvent(
                        NotificationEvent.NetworkErrorEvent(error as NetworkError)
                    )
                }
        }
    }
}
