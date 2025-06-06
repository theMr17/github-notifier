package com.notifier.app.notification.presentation

import com.notifier.app.core.presentation.BaseViewModel
import com.notifier.app.notification.domain.NotificationDataSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationDataSource: NotificationDataSource,
) : BaseViewModel<NotificationState, NotificationEvent, NotificationAction>(NotificationState()) {
    override fun onAction(action: NotificationAction) {
        when (action) {
            is NotificationAction.OnNotificationItemClick -> {
                // Handle notification item click, e.g., navigate to details
            }
        }
    }
}
