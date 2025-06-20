package com.notifier.app.core.presentation.notification

/**
 * Represents the current state of the notification permission.
 *
 * This interface is used to check whether notification permission has been granted,
 * whether a rationale should be shown to the user, and to trigger the permission request flow.
 */
interface NotificationPermissionState {
    /**
     * Indicates whether the app should display a rationale for requesting the notification permission.
     *
     * This is typically true if the user has previously denied the permission without selecting
     * "Don't ask again."
     */
    val shouldShowRationale: Boolean

    /**
     * Indicates whether the notification permission has been granted.
     */
    val isGranted: Boolean

    /**
     * Requests the notification permission from the user.
     *
     * This should be called when the app determines that it needs permission
     * and wants to prompt the user for it.
     */
    fun requestPermission()
}
