package com.notifier.app.core.presentation.notification

/**
 * A fake implementation of [NotificationPermissionState] for use in tests.
 *
 * @param isGranted whether the notification permission is granted.
 * @param shouldShowRationale whether the system should show a rationale for the permission.
 *
 * This implementation is used to simulate various permission states in UI tests without
 * requiring actual system permission dialogs.
 */
class FakeNotificationPermissionState(
    override val isGranted: Boolean,
    override val shouldShowRationale: Boolean,
) : NotificationPermissionState {
    /** No-op implementation since this is only used in tests. */
    override fun requestPermission() {
        /* No-op for tests */
    }
}
